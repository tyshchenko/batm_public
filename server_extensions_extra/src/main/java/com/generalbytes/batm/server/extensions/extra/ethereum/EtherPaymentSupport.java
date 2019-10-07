/*************************************************************************************
 * Copyright (C) 2014-2018 GENERAL BYTES s.r.o. All rights reserved.
 *
 * This software may be distributed and modified under the terms of the GNU
 * General Public License version 2 (GPL2) as published by the Free Software
 * Foundation and appearing in the file GPL2.TXT included in the packaging of
 * this file. Please note that GPL2 Section 2[b] requires that all works based
 * on this software must also be made publicly available under the terms of
 * the GPL2 ("Copyleft").
 *
 * Contact information
 * -------------------
 *
 * GENERAL BYTES s.r.o.
 * Web      :  http://www.generalbytes.com
 *
 ************************************************************************************/
package com.generalbytes.batm.server.extensions.extra.ethereum;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.IExtensionContext;
import com.generalbytes.batm.server.extensions.IWallet;
import com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.EtherScan;
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

public class EtherPaymentSupport implements IPaymentSupport {
    private static final Logger log = LoggerFactory.getLogger(EtherPaymentSupport.class);
    private final Map<String, PaymentRequest> requests = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    protected EtherScan etherScan = new EtherScan();

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
        String address = null;
        String destinationAddress = spec.getOutputs().get(0).getAddress();
        if (spec.isDoNotForward()) {
            final address = destinationAddress;
        } else {
            final address = wallet.getCryptoAddress(spec.getCryptoCurrency());
        }
        long validTillMillis = System.currentTimeMillis() + (spec.getValidInSeconds() * 1000);

        PaymentRequest request = new PaymentRequest(spec.getCryptoCurrency(), spec.getDescription(), validTillMillis,
            address, spec.getTotal(), BigDecimal.ZERO, spec.getRemoveAfterNumberOfConfirmationsOfIncomingTransaction(),
            spec.getRemoveAfterNumberOfConfirmationsOfOutgoingTransaction(), wallet);

        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(() -> {
            try {
                EtherScan.AddressBalance addressBalance = etherScan.getEthAddressBalance(address, spec.getCryptoCurrency());

                if ((addressBalance.receivedAmount.compareTo(BigDecimal.ZERO) > 0) && (request.getState() == PaymentRequest.STATE_NEW)) {
                    log.info("Received: {}, Requested: {}, {}", addressBalance.receivedAmount, spec.getTotal(), request);
                    if (addressBalance.receivedAmount.compareTo(spec.getTotal()) == 0) {
                        if (spec.isDoNotForward()) {
                            log.info("Amounts matches {} do not forward", request);
                            setState(request, PaymentRequest.STATE_SEEN_TRANSACTION);
                        } else {
                            log.info("Amounts matches {}", request);
                            setState(request, PaymentRequest.STATE_SEEN_TRANSACTION);
                            
                            String forwardingState = wallet.sendCoins(destinationAddress, spec.getTotal(), spec.getCryptoCurrency(), "");
                            log.info("Transaction forwarded {}", forwardingState);
                        }
                    } else if (request.getState() != PaymentRequest.STATE_TRANSACTION_INVALID) {
                        log.info("Received amount does not match the requested amount");
                        setState(request, PaymentRequest.STATE_TRANSACTION_INVALID);
                    }
                }
                if ((request.getState() == PaymentRequest.STATE_SEEN_TRANSACTION) || (request.getState() == PaymentRequest.STATE_SEEN_IN_BLOCK_CHAIN)) {
                    if (addressBalance.confirmations > 0) {
                        log.info("Received: {}, Confirmations: {} for {}", addressBalance.receivedAmount, addressBalance.confirmations, request);
                        if (request.getState() == PaymentRequest.STATE_SEEN_TRANSACTION) {
                            setState(request, PaymentRequest.STATE_SEEN_IN_BLOCK_CHAIN);
                        }

                        fireNumberOfConfirmationsChanged(request, addressBalance.confirmations);
                        if (addressBalance.receivedAmount.compareTo(spec.getTotal()) < 0) {
                            fireNumberOfOutConfirmationsChanged(request, addressBalance.confirmations);
                        }
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
        PaymentReceipt result = new PaymentReceipt(CryptoCurrency.ETH.getCode(), paymentAddress);
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
