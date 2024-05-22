package com.generalbytes.batm.server.extensions.extra.ethereum.erc20.usdc;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.IExtensionContext;
import com.generalbytes.batm.server.extensions.IWallet;
import com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.TokenScan;
import com.generalbytes.batm.server.extensions.payment.IPaymentRequestListener;
import com.generalbytes.batm.server.extensions.payment.IPaymentRequestSpecification;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;
import com.generalbytes.batm.server.extensions.payment.PaymentReceipt;
import com.generalbytes.batm.server.extensions.payment.PaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UsdcPaymentSupport implements IPaymentSupport {
    private static final Logger log = LoggerFactory.getLogger(UsdcPaymentSupport.class);
    private final Map<String, PaymentRequest> requests = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    protected TokenScan tokenScan = new TokenScan();

    @Override
    public boolean init(IExtensionContext context) {
        return true;
    }

    @Override
    public PaymentRequest createPaymentRequest(IPaymentRequestSpecification spec) {
        IWallet wallet = spec.getWallet();

        if (spec.getOutputs().size() != 1) {
            throw new IllegalStateException("Only 1 output supported");
        }
        String address = spec.getOutputs().get(0).getAddress();

        long validTillMillis = System.currentTimeMillis() + (spec.getValidInSeconds() * 1000);
        log.info("PaymentRequest {} {} {} {}", spec.getCryptoCurrency(), spec.getDescription(), spec.getTotal(), address);

        PaymentRequest request = new PaymentRequest(spec.getCryptoCurrency(), spec.getDescription(), validTillMillis,
            address, spec.getTotal(), BigDecimal.ZERO, spec.getRemoveAfterNumberOfConfirmationsOfIncomingTransaction(),
            spec.getRemoveAfterNumberOfConfirmationsOfOutgoingTransaction(), wallet);

        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(() -> {
            try {
                TokenScan.AddressBalance addressBalance = tokenScan.getTokenBalance(address, spec.getCryptoCurrency(), spec.getDescription(), spec.getTotal());

                if (addressBalance.receivedAmount.compareTo(BigDecimal.ZERO) > 0) {
                    log.info("Received: {}, Requested: {}, {}", addressBalance.receivedAmount, spec.getTotal(), request);
                    boolean matchInTolerance = false;
                    BigDecimal tolerance = new BigDecimal("0.2");
                    if (addressBalance.receivedAmount.compareTo(spec.getTotal()) == 0) {
                        matchInTolerance = true;
                    } else if (addressBalance.receivedAmount.compareTo(spec.getTotal()) < 0) { //customer sent less coins
                            if (addressBalance.receivedAmount.add(tolerance).compareTo(spec.getTotal()) >= 0) {
                                matchInTolerance = true;
                            }
                    } else if (addressBalance.receivedAmount.compareTo(spec.getTotal()) > 0) { //customer sent more coins
                            if (addressBalance.receivedAmount.subtract(tolerance).compareTo(spec.getTotal()) <= 0) {
                                matchInTolerance = true;
                            }
                    }

                    if (matchInTolerance)  {
                        if(request.getState() == PaymentRequest.STATE_NEW) {
                            log.info("Amounts matches {}", request);
                            setState(request, PaymentRequest.STATE_SEEN_TRANSACTION);
                        }
                        if (addressBalance.confirmations > 0) {
                            if (request.getState() == PaymentRequest.STATE_SEEN_TRANSACTION) {
                                setState(request, PaymentRequest.STATE_SEEN_IN_BLOCK_CHAIN);
                            }
                            log.info("{} confirmations for {}", addressBalance.confirmations, request);
                            fireNumberOfConfirmationsChanged(request, addressBalance.confirmations);
                        }
                    } else if (request.getState() != PaymentRequest.STATE_TRANSACTION_INVALID) {
                        log.info("Received amount does not match the requested amount");
                        setState(request, PaymentRequest.STATE_TRANSACTION_INVALID);
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
        PaymentReceipt result = new PaymentReceipt(CryptoCurrency.USDC.getCode(), paymentAddress);
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
