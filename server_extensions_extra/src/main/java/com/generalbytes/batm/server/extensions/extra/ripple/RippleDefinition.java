package com.generalbytes.batm.server.extensions.extra.ripple;

import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class RippleDefinition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new RipplePaymentSupport();

    public RippleDefinition() {
        super(CryptoCurrency.XRP.getCode(), "Ripple", "ripple","https://ripple.org");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
