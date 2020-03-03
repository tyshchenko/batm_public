package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.binancez;

import java.util.List;

public class BinanceZResponse {
    private List<BinanceZAssetData> balances;
    private int makerCommission;

    public List<BinanceZAssetData> getBalance() {
        return balances;
    }

    public void setBalances(List<BinanceZAssetData> balances) {

        this.balances = balances;
    }

    public int getMakerCommission() {
    	return makerCommission;
    }

    public void setMakerCommission(int makerCommission) {
    	this.makerCommission = makerCommission;
    }

}
