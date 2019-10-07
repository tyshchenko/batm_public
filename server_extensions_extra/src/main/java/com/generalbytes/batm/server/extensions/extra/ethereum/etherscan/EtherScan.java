package com.generalbytes.batm.server.extensions.extra.ethereum.etherscan;

import com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.dto.GetTokenTransactionsResponse;
import com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.dto.GetEthBalanceResponse;
import com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.dto.GetEthTxListResponse;
import si.mazi.rescu.RestProxyFactory;

import java.math.BigDecimal;

public class EtherScan {

    protected IEtherscanAPI etherScanApi = RestProxyFactory.createProxy(IEtherscanAPI.class, "https://api.etherscan.io");

    public AddressBalance getAddressBalance(String address, String cryptoCurrency) {

        GetTokenTransactionsResponse tokenTransactions = etherScanApi.getTokenTransactions("account", "tokentx", address);

        BigDecimal receivedAmount = tokenTransactions.result.stream()
            .filter(tx -> tx.tokenSymbol.equals(cryptoCurrency))
            .map(tx -> new BigDecimal(tx.value).movePointLeft(Integer.parseInt(tx.tokenDecimal)))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        int confirmations = tokenTransactions.result.stream()
            .mapToInt(tx -> Integer.parseInt(tx.confirmations))
            .min()
            .orElse(0);

        return new AddressBalance(receivedAmount, confirmations);
    }

    public AddressBalance getEthAddressBalance(String address, String cryptoCurrency) {

        GetEthTxListResponse ethTxLists = etherScanApi.getEthTxList("account", "txlist", address);

        BigDecimal receivedAmount = ethTxLists.result.stream()
            .filter(tx -> tx.isError.equals("0"))
            .map(tx -> new BigDecimal(tx.value).movePointLeft(18))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        int confirmations = ethTxLists.result.stream()
            .mapToInt(tx -> Integer.parseInt(tx.confirmations))
            .min()
            .orElse(0);

        return new AddressBalance(receivedAmount, confirmations);
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
