package com.generalbytes.batm.server.extensions.extra.nano;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.ICryptoAddressValidator;
import com.generalbytes.batm.server.extensions.extra.bitcoincash.test.PRS;
import com.generalbytes.batm.server.extensions.extra.common.AbstractRPCPaymentSupport;
import com.generalbytes.batm.server.extensions.extra.common.RPCClient;
import com.generalbytes.batm.server.extensions.payment.IPaymentRequestListener;
import com.generalbytes.batm.server.extensions.payment.PaymentRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException;

public class NanoPaymentSupport extends AbstractRPCPaymentSupport {
    private NanoAddressValidator addressValidator = new NanoAddressValidator();

    private static final long MAXIMUM_WAIT_FOR_POSSIBLE_REFUND_MILLIS = TimeUnit.DAYS.toMillis(3); // 3 days
    private static final long MAXIMUM_WATCHING_TIME_MILLIS = TimeUnit.DAYS.toMillis(3); // 3 days (exactly plus Sell Offer Expiration 5-120 minutes)
    private static final BigDecimal TOLERANCE = new BigDecimal("0.0002"); // Received amount should be  cryptoTotalToSend +- tolerance

    @Override
    public String getCurrency() {
        return CryptoCurrency.NANO.getCode();
    }

    @Override
    public long getMaximumWatchingTimeMillis() {
        return MAXIMUM_WATCHING_TIME_MILLIS;
    }

    @Override
    public long getMaximumWaitForPossibleRefundInMillis() {
        return MAXIMUM_WAIT_FOR_POSSIBLE_REFUND_MILLIS;
    }

    @Override
    public BigDecimal getTolerance() {
        return TOLERANCE;
    }

    @Override
    public BigDecimal getMinimumNetworkFee(RPCClient client) {
        return client.getNetworkInfo().relayFee();
    }

    @Override
    public ICryptoAddressValidator getAddressValidator() {
        return addressValidator;
    }

    @Override
    public int calculateTransactionSize(int numberOfInputs, int numberOfOutputs) {
        return (numberOfInputs * 149) + (numberOfOutputs * 34) + 10;
    }

    @Override
    public BigDecimal calculateTxFee(int numberOfInputs, int numberOfOutputs, RPCClient client) {
        final int transactionSize = calculateTransactionSize(numberOfInputs, numberOfOutputs);
        try {
            BigDecimal estimate = new BigDecimal(client.getEstimateFee(2));
            if (BigDecimal.ZERO.compareTo(estimate) == 0 || estimate.compareTo(new BigDecimal("-1")) == 0 ) {
                //bitcoind is clueless
                return getMinimumNetworkFee(client);
            }
            return estimate.divide(new BigDecimal("1000"), RoundingMode.UP).multiply(new BigDecimal(transactionSize));
        } catch (BitcoinRPCException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getSigHashType() {
        return "ALL";
    }



}
