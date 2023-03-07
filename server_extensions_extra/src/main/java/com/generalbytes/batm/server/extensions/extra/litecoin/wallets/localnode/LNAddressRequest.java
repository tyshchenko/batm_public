package com.generalbytes.batm.server.extensions.extra.litecoin.wallets.localnode;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LNAddressRequest {

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("label")
    private String label;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LNAddressRequest(BigDecimal amount, String label) {
        this.label = label;
        this.amount = amount;
    }
}
