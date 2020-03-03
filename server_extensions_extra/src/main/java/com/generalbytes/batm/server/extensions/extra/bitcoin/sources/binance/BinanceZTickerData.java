package com.generalbytes.batm.server.extensions.extra.bitcoin.sources.binance;

import java.math.BigDecimal;

public class BinanceZTickerData {
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}