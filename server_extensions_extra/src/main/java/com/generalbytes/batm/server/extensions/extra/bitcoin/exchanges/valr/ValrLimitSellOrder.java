package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.math.BigDecimal;

public class ValrLimitSellOrder {

    @JsonProperty("pair")
    private String pair;

    @JsonProperty("side")
    private String side;

    @JsonProperty("volume")
    private String volume;

    @JsonProperty("price")
    private String price;

    public void setPair(String pair) {
        this.pair = pair;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void setAmount(String volume) {
        this.volume = volume;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
