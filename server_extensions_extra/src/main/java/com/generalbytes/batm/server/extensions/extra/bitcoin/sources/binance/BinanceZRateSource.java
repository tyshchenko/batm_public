/*************************************************************************************
 * Copyright (C) 2014-2019 GENERAL BYTES s.r.o. All rights reserved.
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
package com.generalbytes.batm.server.extensions.extra.bitcoin.sources.binance;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.common.currencies.FiatCurrency;
import com.generalbytes.batm.server.extensions.IRateSource;
import com.generalbytes.batm.server.extensions.extra.bitcoin.sources.luno.LunoRateSource;

import si.mazi.rescu.RestProxyFactory;

import java.math.BigDecimal;
import java.util.*;

public class BinanceZRateSource implements IRateSource {

    private BinanceZAPI api;
    private LunoRateSource luno;
    private String preferredFiatCurrency = FiatCurrency.ZAR.getCode();

    public BinanceZRateSource(String preferedFiatCurrency) {
        api = RestProxyFactory.createProxy(BinanceZAPI.class, "https://api.binance.com");
        luno = new LunoRateSource(preferedFiatCurrency);
        if (FiatCurrency.ZAR.getCode().equalsIgnoreCase(preferedFiatCurrency)) {
            this.preferredFiatCurrency = FiatCurrency.ZAR.getCode();
        }

    }

    @Override
    public Set<String> getCryptoCurrencies() {
        Set<String> result = new HashSet<>();
        result.add(CryptoCurrency.DASHD.getCode());
        result.add(CryptoCurrency.USDT.getCode());
        return result;
    }

    @Override
    public Set<String> getFiatCurrencies() {
        Set<String> result = new HashSet<>();
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
        String cryptoZCurrency = cryptoCurrency + "BTC";
        BigDecimal priceInBtc = new BigDecimal(0);

        if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
            cryptoZCurrency = "DASHBTC";
            BinanceZTickerData selectedCryptoInBtc = api.getTicker(cryptoZCurrency);
            priceInBtc = selectedCryptoInBtc.getPrice();

        }
        if (CryptoCurrency.USDT.getCode().equalsIgnoreCase(cryptoCurrency)) {
            cryptoZCurrency = "BTCUSDT";
            BinanceZTickerData selectedCryptoInBtc = api.getTicker(cryptoZCurrency);
            BigDecimal one = new BigDecimal(1);
            BigDecimal usdtprice = selectedCryptoInBtc.getPrice();
            priceInBtc = one.divide(usdtprice);
        }
        BigDecimal priceBTCZAR = luno.getExchangeRateLast("BTC", "ZAR");

        return priceBTCZAR.multiply(priceInBtc).setScale(2, BigDecimal.ROUND_CEILING);
    }
}