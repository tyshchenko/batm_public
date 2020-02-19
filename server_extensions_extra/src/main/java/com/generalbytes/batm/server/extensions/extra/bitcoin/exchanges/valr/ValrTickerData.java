/*************************************************************************************
 * Copyright (C) 2014-2018 GENERAL BYTES s.r.o. All rights reserved.
 *
 * This software may be distributed and modified under the terms of the GNU
 * General Public License version 2 (GPL2) as published by the Free Software
 * Foundation and appearing in the file GPL2.TXT included in the packaging of
 * this file. Please note that GPL2 Section 2[b] requires that all works based
 * on this software must also be made publicly available under the terms of
 * the GPL2 ("Copyleft").
 *
 * Contact information
 * -------------------
 *
 * GENERAL BYTES s.r.o.
 * Web      :  http://www.generalbytes.com
 *
 ************************************************************************************/
package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ValrTickerData {

    @JsonProperty("currencyPair")
    private String currencyPair;

    @JsonProperty("created")
    private String created;

    @JsonProperty("bidPrice")
    private BigDecimal bidPrice;

    @JsonProperty("askPrice")
    private BigDecimal askPrice;

    @JsonProperty("lastTradedPrice")
    private BigDecimal lastTradedPrice;

    @JsonProperty("baseVolume")
    private BigDecimal baseVolume;


    public BigDecimal getBid() {
        return bidPrice;
    }

    public BigDecimal getAsk() {
        return askPrice;
    }

    public void setPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getPrice() {
        return bidPrice;
    }

}
