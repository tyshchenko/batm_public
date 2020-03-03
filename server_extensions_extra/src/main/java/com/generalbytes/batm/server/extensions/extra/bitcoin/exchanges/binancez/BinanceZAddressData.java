package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.binancez;

import java.math.BigDecimal;

public class BinanceZAddressData {
    private String address;
    private Boolean success;
    private String msg;

    public String getAddress() {
        return address;
    }
    public String getMsg() {
        return msg;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}