package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.math.BigDecimal;

public class ValrSellOrder {

    @JsonProperty("pair")
    private String pair;

    @JsonProperty("side")
    private BigDecimal side;

    @JsonProperty("baseAmount")
    private String baseAmount;

    public void setPair(String pair) {
        this.pair = pair;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void setAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

}
