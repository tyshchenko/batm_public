package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.binancez;

public class BinanceZSendCoinResponse {
    private Boolean success;
    private String msg;
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
