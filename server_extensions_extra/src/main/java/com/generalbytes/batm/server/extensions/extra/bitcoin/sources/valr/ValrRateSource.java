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
package com.generalbytes.batm.server.extensions.extra.bitcoin.sources.valr;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.common.currencies.FiatCurrency;
import com.generalbytes.batm.server.extensions.IRateSource;
import com.generalbytes.batm.server.extensions.IRateSourceAdvanced;

import si.mazi.rescu.RestProxyFactory;

import java.math.BigDecimal;
import java.util.*;

public class ValrRateSource implements IRateSourceAdvanced {

    private ValrAPI api;
    private String preferredFiatCurrency = FiatCurrency.ZAR.getCode();

    public ValrRateSource(String preferedFiatCurrency) {
        api = RestProxyFactory.createProxy(ValrAPI.class, "https://api.valr.com");

        if (FiatCurrency.ZAR.getCode().equalsIgnoreCase(preferedFiatCurrency)) {
            this.preferredFiatCurrency = FiatCurrency.ZAR.getCode();
        }

    }

    @Override
    public Set<String> getCryptoCurrencies() {
        Set<String> result = new HashSet<String>();
        result.add(CryptoCurrency.BTC.getCode());
        result.add(CryptoCurrency.ETH.getCode());
        result.add(CryptoCurrency.XRP.getCode());
        result.add(CryptoCurrency.DASHD.getCode());
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
            final ValrTickerData btcZar = api.getTicker("BTCZAR");
            BigDecimal lastBtcPriceInZar = btcZar.getAskPrice();
            return lastBtcPriceInZar;
        } else if (CryptoCurrency.ETH.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData ethZar = api.getTicker("ETHZAR");
            BigDecimal lastEthPriceInZar = ethZar.getAskPrice();
            return lastEthPriceInZar;
        } else if (CryptoCurrency.XRP.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData xrpZar = api.getTicker("XRPZAR");
            BigDecimal lastxrpPriceInZar = xrpZar.getAskPrice();
            return lastxrpPriceInZar;
        } else if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData btcZar = api.getTicker("BTCZAR");
            BigDecimal lastBtcPriceInZar = btcZar.getAskPrice();
            final ValrTickerData btcDash = api.getTicker("DASHBTC");
            BigDecimal lastDashPriceInBtc = btcDash.getAskPrice();
            return lastBtcPriceInZar.multiply(lastDashPriceInBtc);
        }
        return null;
    }

    @Override
    public BigDecimal getExchangeRateForBuy(String cryptoCurrency, String fiatCurrency) {
        if (!getFiatCurrencies().contains(fiatCurrency)) {
            return null;
        }
        if (CryptoCurrency.BTC.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData btcZar = api.getTicker("BTCZAR");
            BigDecimal lastBtcPriceInZar = btcZar.getAskPrice();
            return lastBtcPriceInZar;
        } else if (CryptoCurrency.ETH.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData ethZar = api.getTicker("ETHZAR");
            BigDecimal lastEthPriceInZar = ethZar.getAskPrice();
            return lastEthPriceInZar;
        } else if (CryptoCurrency.XRP.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData ethZar = api.getTicker("XRPZAR");
            BigDecimal lastEthPriceInZar = ethZar.getAskPrice();
            return lastEthPriceInZar;
        } else if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData btcZar = api.getTicker("BTCZAR");
            BigDecimal lastBtcPriceInZar = btcZar.getAskPrice();
            final ValrTickerData btcDash = api.getTicker("DASHBTC");
            BigDecimal lastDashPriceInBtc = btcDash.getAskPrice();
            return lastBtcPriceInZar.multiply(lastDashPriceInBtc);
        }
        return null;
    }

    @Override
    public BigDecimal getExchangeRateForSell(String cryptoCurrency, String fiatCurrency) {
        if (!getFiatCurrencies().contains(fiatCurrency)) {
            return null;
        }
        if (CryptoCurrency.BTC.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData btcZar = api.getTicker("BTCZAR");
            BigDecimal lastBtcPriceInZar = btcZar.getPrice();
            return lastBtcPriceInZar;
        } else if (CryptoCurrency.ETH.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData ethZar = api.getTicker("ETHZAR");
            BigDecimal lastEthPriceInZar = ethZar.getPrice();
            return lastEthPriceInZar;
        } else if (CryptoCurrency.XRP.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData ethZar = api.getTicker("XRPZAR");
            BigDecimal lastEthPriceInZar = ethZar.getPrice();
            return lastEthPriceInZar;
        } else if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
            final ValrTickerData btcZar = api.getTicker("BTCZAR");
            BigDecimal lastBtcPriceInZar = btcZar.getPrice();
            final ValrTickerData btcDash = api.getTicker("DASHBTC");
            BigDecimal lastDashPriceInBtc = btcDash.getPrice();
            return lastBtcPriceInZar.multiply(lastDashPriceInBtc);
        }
        return null;
    }
    @Override
    public BigDecimal calculateBuyPrice(String cryptoCurrency, String fiatCurrency, BigDecimal cryptoAmount) {
        final BigDecimal rate = getExchangeRateForBuy(cryptoCurrency, fiatCurrency);
        if (rate != null) {
            return rate.multiply(cryptoAmount);
        }
        return null;
    }

    @Override
    public BigDecimal calculateSellPrice(String cryptoCurrency, String fiatCurrency, BigDecimal cryptoAmount) {
        final BigDecimal rate = getExchangeRateForSell(cryptoCurrency, fiatCurrency);
        if (rate != null) {
            return rate.multiply(cryptoAmount);
        }
        return null;
    }
}
