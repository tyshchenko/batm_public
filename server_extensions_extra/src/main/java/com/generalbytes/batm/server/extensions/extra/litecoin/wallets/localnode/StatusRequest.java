package com.generalbytes.batm.server.extensions.extra.litecoin.wallets.localnode;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusRequest {

    @JsonProperty("amount")
    private BigDecimal amount;



    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
