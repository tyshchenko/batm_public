package com.generalbytes.batm.server.extensions.extra.ethereum.etherscan;

import si.mazi.rescu.RestProxyFactory;
import java.math.BigDecimal;

public class TokenScan {

    protected IOurAPI ourApi = RestProxyFactory.createProxy(IOurAPI.class, "http://127.0.0.1:8099/");


    public AddressBalance getTokenBalance(String address, String cryptoCurrency, String label, BigDecimal amount) {
        BalanceData result = ourApi.getlabelstatus(cryptoCurrency, address, label, amount.toString());
        return new AddressBalance(result.getBalance(), result.getConfirmation());
    }

    public class AddressBalance {
        public final BigDecimal receivedAmount;
        public final int confirmations;

        public AddressBalance(BigDecimal receivedAmount, int confirmations) {
            this.receivedAmount = receivedAmount;
            this.confirmations = confirmations;
        }
    }
}
