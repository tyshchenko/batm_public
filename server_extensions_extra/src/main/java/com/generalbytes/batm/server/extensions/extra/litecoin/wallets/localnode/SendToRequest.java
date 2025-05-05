package com.generalbytes.batm.server.extensions.extra.litecoin.wallets.localnode;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SendToRequest {

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("address")
    private String address;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SendToRequest(BigDecimal amount, String address) {
        this.address = address;
        this.amount = amount;
    }
}
