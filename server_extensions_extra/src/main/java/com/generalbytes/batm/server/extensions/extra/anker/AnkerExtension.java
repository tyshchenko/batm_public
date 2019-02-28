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
import com.generalbytes.batm.server.extensions.extra.anker.wallets.ankerd.AnkerRPCWallet;
import com.generalbytes.batm.server.extensions.extra.anker.exchanges.luno.LunoExchange;

import java.math.BigDecimal;
import java.util.*;

public class AnkerExtension extends AbstractExtension {
    @Override
    public String getName() {
        return "BATM Anker extra extension";
    }

    @Override
    public IWallet createWallet(String walletLogin) {
        if (walletLogin != null && !walletLogin.trim().isEmpty()) {
            StringTokenizer st = new StringTokenizer(walletLogin, ":");
            String walletType = st.nextToken();

            if ("ankerd".equalsIgnoreCase(walletType)) {
                String protocol = st.nextToken();
                String username = st.nextToken();
                String password = st.nextToken();
                String hostname = st.nextToken();
                String port = st.nextToken();
                String accountName = "";
                if (st.hasMoreTokens()) {
                    accountName = st.nextToken();
                }


                if (protocol != null && username != null && password != null && hostname != null && port != null && accountName != null) {
                    String rpcURL = protocol + "://" + username + ":" + password + "@" + hostname + ":" + port;
                    return new AnkerRPCWallet(rpcURL, accountName);
                }
            }
            if ("ankerdemo".equalsIgnoreCase(walletType)) {

                String fiatCurrency = st.nextToken();
                String walletAddress = "";
                if (st.hasMoreTokens()) {
                    walletAddress = st.nextToken();
                }

                if (fiatCurrency != null && walletAddress != null) {
                    return new DummyExchangeAndWalletAndSource(fiatCurrency, CryptoCurrency.ANK.getCode(), walletAddress);
                }
            }
        }
        return null;
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
    public ICryptoAddressValidator createAddressValidator(String cryptoCurrency) {
        if (CryptoCurrency.ANK.getCode().equalsIgnoreCase(cryptoCurrency)) {
            return new AnkerAddressValidator();
        }
        return null;
    }

    @Override
    public IRateSource createRateSource(String sourceLogin) {
        if (sourceLogin != null && !sourceLogin.trim().isEmpty()) {
            StringTokenizer st = new StringTokenizer(sourceLogin, ":");
            String exchangeType = st.nextToken();
            if ("ankFix".equalsIgnoreCase(exchangeType)) {
                BigDecimal rate = BigDecimal.ZERO;
                if (st.hasMoreTokens()) {
                    try {
                        rate = new BigDecimal(st.nextToken());
                    } catch (Throwable e) {
                    }
                }
                String preferedFiatCurrency = FiatCurrency.ZAR.getCode();
                if (st.hasMoreTokens()) {
                    preferedFiatCurrency = st.nextToken().toUpperCase();
                }
                return new FixPriceRateSource(rate, preferedFiatCurrency);
            }
            else if ("lunoRateSource".equalsIgnoreCase(exchangeType)) {
                String preferedFiatCurrency = FiatCurrency.ZAR.getCode();
                return new LunoRateSource(preferedFiatCurrency);
            }
        }
        return null;
    }

    @Override
    public Set<String> getSupportedCryptoCurrencies() {
        Set<String> result = new HashSet<String>();
        result.add(CryptoCurrency.ANK.getCode());
        return result;
    }

}