package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.math.BigDecimal;

public class ValrBuyOrder {

    @JsonProperty("pair")
    private String pair;

    @JsonProperty("side")
    private String side;

    @JsonProperty("quoteAmount")
    private String quoteAmount;

    public void setPair(String pair) {
        this.pair = pair;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void setAmount(String quoteAmount) {
        this.quoteAmount = quoteAmount;
    }

}
