package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.math.BigDecimal;

public class ValrSendXRP {

    @JsonProperty("address")
    private String address;

    @JsonProperty("paymentReference")
    private String paymentReference;

    @JsonProperty("amount")
    private String amount;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
