package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.math.BigDecimal;

public class ValrLimitBuyOrder {

    @JsonProperty("side")
    private String side;

    @JsonProperty("quantity")
    private String quantity;

    @JsonProperty("price")
    private String price;

    @JsonProperty("pair")
    private String pair;

    public void setPair(String pair) {
        this.pair = pair;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void setAmount(String quantity) {
        this.quantity = quantity;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
