package com.generalbytes.batm.server.extensions.extra.nano;

import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class NanoDefinition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new NanoPaymentSupport();

    public NanoDefinition() {
        super(CryptoCurrency.NANO.getCode(), "Nano", "nano","https://nano.org");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
