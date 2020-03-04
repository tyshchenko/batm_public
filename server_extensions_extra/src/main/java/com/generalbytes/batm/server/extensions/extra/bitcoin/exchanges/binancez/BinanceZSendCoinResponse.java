package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.binancez;

public class BinanceZSendCoinResponse {
    private Boolean success;
    private String msg;
    private String id;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public String getId() {
        return id;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
