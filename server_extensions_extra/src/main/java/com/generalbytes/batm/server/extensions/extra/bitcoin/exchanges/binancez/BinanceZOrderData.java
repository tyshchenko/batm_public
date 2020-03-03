package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.binancez;

import java.math.BigDecimal;

public class BinanceZOrderData {
    private String orderId;
    private String msg;

    public String getOrderId() {
        return orderId;
    }
    public String getMsg() {
        return msg;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}