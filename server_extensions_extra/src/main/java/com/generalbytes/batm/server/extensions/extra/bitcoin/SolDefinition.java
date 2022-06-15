package com.generalbytes.batm.server.extensions.extra.bitcoin;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.CryptoCurrencyDefinition;
import com.generalbytes.batm.server.extensions.payment.IPaymentSupport;

public class SolDefinition extends CryptoCurrencyDefinition{
    private IPaymentSupport paymentSupport = new SolPaymentSupport();

    public SolDefinition() {
        super(CryptoCurrency.SOL.getCode(), "Solana", "solana","https://sol.so");
    }

    @Override
    public IPaymentSupport getPaymentSupport() {
        return paymentSupport;
    }
}
