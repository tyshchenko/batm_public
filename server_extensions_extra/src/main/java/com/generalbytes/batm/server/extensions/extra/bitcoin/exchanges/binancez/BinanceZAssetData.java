package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.binancez;

import java.math.BigDecimal;

public class BinanceZAssetData {
    private String asset;
    private BigDecimal free;

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getFree() {
        return free;
    }

    public void setFree(BigDecimal free) {
        this.free = free;
    }
}
