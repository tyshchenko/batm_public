package com.generalbytes.batm.server.extensions.extra.bitcoin;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class TrxDefinition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new TrxPaymentSupport();

    public TrxDefinition() {
        super(CryptoCurrency.TRX.getCode(), "Tron", "tron","https://tron.com");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
