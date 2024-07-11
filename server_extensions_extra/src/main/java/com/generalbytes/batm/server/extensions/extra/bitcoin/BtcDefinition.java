package com.generalbytes.batm.server.extensions.extra.bitcoin;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class BtcDefinition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new BtcPaymentSupport();

    public BtcDefinition() {
        super(CryptoCurrency.BTC.getCode(), "Bitcoin", "bitcoin","https://btc.com");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
