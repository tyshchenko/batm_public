/*************************************************************************************
 * Copyright (C) 2014-2016 GENERAL BYTES s.r.o. All rights reserved.
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
package com.generalbytes.batm.server.extensions.extra.anker;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.common.currencies.FiatCurrency;
import com.generalbytes.batm.server.extensions.*;
import com.generalbytes.batm.server.extensions.FixPriceRateSource;
import com.generalbytes.batm.server.extensions.extra.anker.sources.luno.LunoRateSource;
import com.generalbytes.batm.server.extensions.extra.anker.exchanges.luno.LunoExchange;

import java.math.BigDecimal;
import java.util.*;

public class AnkerExtension extends AbstractExtension {
    @Override
    public String getName() {
        return "BATM Anker extra extension";
    }


    @Override
    public IExchange createExchange(String sourceLogin) {
        if (sourceLogin != null && !sourceLogin.trim().isEmpty()) {
            StringTokenizer st = new StringTokenizer(sourceLogin, ":");
            String exchangeType = st.nextToken();
            if ("lunoexchange".equalsIgnoreCase(exchangeType)) {
                String apiKey = st.nextToken();
                String apiSecret = st.nextToken();
                String typeorder = st.nextToken();
                String preferredFiatCurrency = FiatCurrency.ZAR.getCode();
                if (st.hasMoreTokens()) {
                    preferredFiatCurrency = st.nextToken().toUpperCase();
                }
                return new LunoExchange(apiKey, apiSecret, preferredFiatCurrency, typeorder);
            }
        }
        return null;
    }

    @Override
    public IRateSource createRateSource(String sourceLogin) {
        if (sourceLogin != null && !sourceLogin.trim().isEmpty()) {
            StringTokenizer st = new StringTokenizer(sourceLogin, ":");
            String exchangeType = st.nextToken();
            if ("lunoRateSource".equalsIgnoreCase(exchangeType)) {
                String preferedFiatCurrency = FiatCurrency.ZAR.getCode();
                return new LunoRateSource(preferedFiatCurrency);
            }
        }
        return null;
    }

    @Override
    public Set<String> getSupportedCryptoCurrencies() {
        Set<String> result = new HashSet<String>();
        result.add(CryptoCurrency.BTC.getCode());
        return result;
    }

}