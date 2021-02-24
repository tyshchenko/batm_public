package com.generalbytes.batm.server.extensions.extra.litecoin.wallets.localnode;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceData {

    @JsonProperty("btcPaid")
    private BigDecimal balance;

    @JsonProperty("status")
    private String status;



    public BigDecimal getBalance() {
        BigDecimal ten = BigDecimal.TEN;
        return balance.divide(ten.pow(8));
    }

    public String getStatus() {
        return status;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
