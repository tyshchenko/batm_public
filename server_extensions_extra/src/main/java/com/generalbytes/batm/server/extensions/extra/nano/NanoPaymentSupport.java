package com.generalbytes.batm.server.extensions.extra.nano;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.ICryptoAddressValidator;
import com.generalbytes.batm.server.extensions.IExtensionContext;
import com.generalbytes.batm.server.extensions.IWallet;
import com.generalbytes.batm.server.extensions.payment.IPaymentRequestListener;
import com.generalbytes.batm.server.extensions.payment.IPaymentRequestSpecification;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;
import com.generalbytes.batm.server.extensions.payment.PaymentReceipt;
import com.generalbytes.batm.server.extensions.payment.PaymentRequest;
import com.generalbytes.batm.server.extensions.extra.nano.wallets.nano.NanoWallet;
import com.generalbytes.batm.server.extensions.extra.nano.wallets.nano.BalanceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class NanoPaymentSupport implements IPaymentSupport {
    private NanoAddressValidator addressValidator = new NanoAddressValidator();

    private static final Logger log = LoggerFactory.getLogger(NanoPaymentSupport.class);
    private final Map<String, PaymentRequest> requests = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public boolean init(IExtensionContext context) {
        return true;
    }


    @Override
    public PaymentRequest createPaymentRequest(IPaymentRequestSpecification spec) {
        IWallet wallet = spec.getWallet();
        NanoWallet nanoWallet = new NanoWallet("ZAR", CryptoCurrency.NANO.getCode());
        if (spec.getOutputs().size() != 1) {
            throw new IllegalStateException("Only 1 output supported");
        }

        String destinationAddress = spec.getOutputs().get(0).getAddress();

        final String address = nanoWallet.getNewCryptoAddress(spec.getCryptoCurrency());
        long validTillMillis = System.currentTimeMillis() + (spec.getValidInSeconds() * 1000);

        PaymentRequest request = new PaymentRequest(spec.getCryptoCurrency(), spec.getDescription(), validTillMillis,
            address, spec.getTotal(), BigDecimal.ZERO, spec.getRemoveAfterNumberOfConfirmationsOfIncomingTransaction(),
            spec.getRemoveAfterNumberOfConfirmationsOfOutgoingTransaction(), wallet);

        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(() -> {
            try {
                BalanceData addressStatus = nanoWallet.getStatus(address);
                int confirmations = Integer.parseInt(addressStatus.getConfirmations());

                if ((addressStatus.getBalance().compareTo(BigDecimal.ZERO) > 0) && (request.getState() == PaymentRequest.STATE_NEW)) {
                    log.info("Received: {}, Requested: {}, {}", addressStatus.getBalance(), spec.getTotal(), request);
                    if (addressStatus.getBalance().compareTo(spec.getTotal()) == 0) {
                        log.info("Amounts matches {}", request);
                        setState(request, PaymentRequest.STATE_SEEN_TRANSACTION);
                    } else if (request.getState() != PaymentRequest.STATE_TRANSACTION_INVALID) {
                        log.info("Received amount does not match the requested amount");
                        setState(request, PaymentRequest.STATE_TRANSACTION_INVALID);
                    }
                }
                if ((request.getState() == PaymentRequest.STATE_SEEN_TRANSACTION) || (request.getState() == PaymentRequest.STATE_SEEN_IN_BLOCK_CHAIN)) {
                    if (confirmations > 0) {
                        log.info("Received: {}, Confirmations: {} for {}", addressStatus.getBalance(), confirmations, request);
                        if (request.getState() == PaymentRequest.STATE_SEEN_TRANSACTION) {
                            setState(request, PaymentRequest.STATE_SEEN_IN_BLOCK_CHAIN);
                        }

                        fireNumberOfConfirmationsChanged(request, confirmations);
                        fireNumberOfOutConfirmationsChanged(request, confirmations);

                    }
                }

            } catch (Exception e) {
                log.error("", e);
            }

        }, 15, 5, TimeUnit.SECONDS);

        executorService.schedule(() -> {
            try {
                scheduledFuture.cancel(false);
                if (request.getState() != PaymentRequest.STATE_SEEN_IN_BLOCK_CHAIN) {
                    log.info("Cancelling {}", request);
                    setState(request, PaymentRequest.STATE_TRANSACTION_TIMED_OUT);
                }
            } catch (Throwable t) {
                log.error("", t);
            }
        }, spec.getValidInSeconds(), TimeUnit.SECONDS);

        requests.entrySet().removeIf(e -> e.getValue().getValidTill() <  System.currentTimeMillis());
        requests.put(address, request);
        return request;
    }


    @Override
    public boolean isPaymentReceived(String paymentAddress) {
        PaymentRequest paymentRequest = requests.get(paymentAddress);
        return paymentRequest != null && paymentRequest.getState() == PaymentRequest.STATE_SEEN_IN_BLOCK_CHAIN;
    }

    @Override
    public PaymentReceipt getPaymentReceipt(String paymentAddress) {
        PaymentReceipt result = new PaymentReceipt(CryptoCurrency.NANO.getCode(), paymentAddress);
        PaymentRequest paymentRequest = requests.get(paymentAddress);
        if (paymentRequest != null && paymentRequest.getState() == PaymentRequest.STATE_SEEN_IN_BLOCK_CHAIN) {
            result.setStatus(PaymentReceipt.STATUS_PAID);
            result.setConfidence(PaymentReceipt.CONFIDENCE_SURE);
            result.setAmount(paymentRequest.getAmount());
            result.setTransactionId(paymentRequest.getIncomingTransactionHash());
        }
        return result;
    }

    private void fireNumberOfConfirmationsChanged(PaymentRequest request, int numberOfConfirmations) {
        IPaymentRequestListener listener = request.getListener();
        if (listener != null) {
            listener.numberOfConfirmationsChanged(request, numberOfConfirmations, IPaymentRequestListener.Direction.INCOMING);
        }
    }

    private void fireNumberOfOutConfirmationsChanged(PaymentRequest request, int numberOfConfirmations) {
        IPaymentRequestListener listener = request.getListener();
        if (listener != null) {
            listener.numberOfConfirmationsChanged(request, numberOfConfirmations, IPaymentRequestListener.Direction.OUTGOING);
        }
    }

    private void setState(PaymentRequest request, int newState) {
        int previousState = request.getState();
        request.setState(newState);
        log.debug("Transaction state changed: {} -> {} {}", previousState, newState, request);

        IPaymentRequestListener listener = request.getListener();
        if (listener != null) {
            listener.stateChanged(request, previousState, request.getState());
        }
    }


}
