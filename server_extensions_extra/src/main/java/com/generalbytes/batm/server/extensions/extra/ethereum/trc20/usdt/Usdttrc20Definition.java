package com.generalbytes.batm.server.extensions.extra.ethereum.trc20.usdt;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class Usdttrc20Definition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new Usdttrc20PaymentSupport();

    public Usdttrc20Definition() {
        super(CryptoCurrency.USDTTRC20.getCode(), "USDT Tether TRC20 Token", "tron","https://usdt.com/");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
