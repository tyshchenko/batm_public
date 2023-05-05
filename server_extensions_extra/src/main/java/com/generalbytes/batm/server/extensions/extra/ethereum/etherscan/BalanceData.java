package com.generalbytes.batm.server.extensions.extra.ethereum.etherscan;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceData {

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("confirmation")
    private String confirmation;



    public BigDecimal getBalance() {
        return balance;
    }

    public int getConfirmation() {
        return confirmation;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
