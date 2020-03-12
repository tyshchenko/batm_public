package com.generalbytes.batm.server.extensions.extra.nano.wallets.nano;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceData {

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("confirmations")
    private String confirmations;

    @JsonProperty("status")
    private String status;

    @JsonProperty("error")
    private String error;


    public BigDecimal getBalance() {
        BigInteger ten = BigInteger.TEN;
        return balance.divide(ten.pow(8));
    }
    public String getConfirmations() {
        return confirmations;
    }
    public String getStatus() {
        return status;
    }
    public String getError() {
        return error;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
