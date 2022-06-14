package com.generalbytes.batm.server.extensions.extra.ethereum.erc20.shib;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class ShibDefinition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new ShibPaymentSupport();

    public ShibDefinition() {
        super(CryptoCurrency.SHIB.getCode(), "SHIB ERC20 Token", "ethereum","https://shib.co");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
