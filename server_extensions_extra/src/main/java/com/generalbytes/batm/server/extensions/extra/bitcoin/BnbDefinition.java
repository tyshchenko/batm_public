package com.generalbytes.batm.server.extensions.extra.bitcoin;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class BnbDefinition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new BnbPaymentSupport();

    public BnbDefinition() {
        super(CryptoCurrency.BNBBSC.getCode(), "Binance coin", "bnb","https://binance.com");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
