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
