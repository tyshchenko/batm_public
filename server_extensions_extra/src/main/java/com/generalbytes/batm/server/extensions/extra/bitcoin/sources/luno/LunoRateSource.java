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
package com.generalbytes.batm.server.extensions.extra.bitcoin.sources.luno;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.common.currencies.FiatCurrency;
import com.generalbytes.batm.server.extensions.IRateSource;

import si.mazi.rescu.RestProxyFactory;

import java.math.BigDecimal;
import java.util.*;

public class LunoRateSource implements IRateSource {

    private LunoAPI api;
    private String preferredFiatCurrency = FiatCurrency.ZAR.getCode();

    public LunoRateSource(String preferedFiatCurrency) {
        api = RestProxyFactory.createProxy(LunoAPI.class, "https://api.mybitx.com");

        if (FiatCurrency.ZAR.getCode().equalsIgnoreCase(preferedFiatCurrency)) {
            this.preferredFiatCurrency = FiatCurrency.ZAR.getCode();
        }

    }

    @Override
    public Set<String> getCryptoCurrencies() {
        Set<String> result = new HashSet<String>();
        result.add(CryptoCurrency.BTC.getCode());
        result.add(CryptoCurrency.ETH.getCode());
        result.add(CryptoCurrency.LTC.getCode());
        result.add(CryptoCurrency.XRP.getCode());
        return result;
    }

    @Override
    public Set<String> getFiatCurrencies() {
        Set<String> result = new HashSet<String>();
        result.add(FiatCurrency.ZAR.getCode());
        return result;
    }

    @Override
    public String getPreferredFiatCurrency() {
        return this.preferredFiatCurrency;
    }

    @Override
    public BigDecimal getExchangeRateLast(String cryptoCurrency, String fiatCurrency) {
        if (!getFiatCurrencies().contains(fiatCurrency)) {
            return null;
        }
        if (CryptoCurrency.BTC.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final LunoTickerData btcZar = api.getTicker("XBTZAR");
            BigDecimal lastBtcPriceInZar = btcZar.getPrice();
            return lastBtcPriceInZar;
        } else if (CryptoCurrency.ETH.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final LunoTickerData ethZar = api.getTicker("ETHZAR");
            BigDecimal lastEthPriceInZar = ethZar.getPrice();
            return lastEthPriceInZar;
        } else if (CryptoCurrency.LTC.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final LunoTickerData ltcZar = api.getTicker("LTCZAR");
            BigDecimal lastLTCPriceInZar = ltcZar.getPrice();
            return lastLTCPriceInZar;
        } else if (CryptoCurrency.XRP.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final LunoTickerData xrpZar = api.getTicker("XRPZAR");
            BigDecimal lastxrpPriceInZar = xrpZar.getPrice();
            return lastxrpPriceInZar;
        }
        return null;
    }
}
