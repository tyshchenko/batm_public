package com.generalbytes.batm.server.extensions.extra.ethereum.etherscan;

import si.mazi.rescu.RestProxyFactory;
import com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.EtherScan;
import java.math.BigDecimal;

public class TokenScan {

    protected IOurAPI ourApi = RestProxyFactory.createProxy(IOurAPI.class, "http://127.0.0.1:8099/");


    public EtherScan.AddressBalance getTokenBalance(String address, String cryptoCurrency, String label, BigDecimal amount) {
        BalanceData result = ourApi.getlabelstatus(cryptoCurrency, address, label, amount.toString());
        return new EtherScan.AddressBalance(result.getBalance(), result.getConfirmation());
    }

}
