package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.math.BigDecimal;

public class ValrBalances {

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("available")
    private BigDecimal available;

    @JsonProperty("reserved")
    private BigDecimal reserved;

    @JsonProperty("total")
    private BigDecimal total;


    public BigDecimal getBalance() {
        BigDecimal avaiableBalance = available;
        return avaiableBalance;
    }

    public String getCurrency() {
        return currency;
    }

}
