package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.math.BigDecimal;

public class ValrSendTron {

    @JsonProperty("address")
    private String address;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("networkType")
    private String networkType;


    public void setAddress(String address) {
        this.address = address;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

}
