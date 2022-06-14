package com.generalbytes.batm.server.extensions.extra.ethereum.erc20.usdc;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class UsdcDefinition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new UsdcPaymentSupport();

    public UsdcDefinition() {
        super(CryptoCurrency.USDC.getCode(), "USDC Stablecoin ERC20 Token", "ethereum","https://usdc.com/");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
