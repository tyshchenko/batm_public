package com.generalbytes.batm.server.extensions.extra.dogecoin;

import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class DogeDefinition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new DogePaymentSupport();

    public DogeDefinition() {
        super(CryptoCurrency.DOGE.getCode(), "Dogecoin", "dogecoin","https://doge.org");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
