package com.generalbytes.batm.server.extensions.extra.nano;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.common.currencies.FiatCurrency;
import com.generalbytes.batm.server.extensions.*;
import com.generalbytes.batm.server.extensions.FixPriceRateSource;
import com.generalbytes.batm.server.extensions.extra.nano.wallets.nanod.NanoRPCWallet;
import com.generalbytes.batm.server.extensions.extra.nano.wallets.nano.NanoWallet;

import java.math.BigDecimal;
import java.util.*;

public class NanoExtension extends AbstractExtension {
    private static final CryptoCurrencyDefinition DEFINITION = new NanoDefinition();


    @Override
    public String getName() {
        return "BATM Nano extra extension";
    }

    @Override
    public IWallet createWallet(String walletLogin) {
        if (walletLogin != null && !walletLogin.trim().isEmpty()) {
            StringTokenizer st = new StringTokenizer(walletLogin, ":");
            String walletType = st.nextToken();

            if ("nanod".equalsIgnoreCase(walletType)) {
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
                    return new NanoRPCWallet(rpcURL, accountName);
                }
            } else
            if ("nanowallet".equalsIgnoreCase(walletType)) {

                String fiatCurrency = st.nextToken();

                if (fiatCurrency != null) {
                    return new NanoWallet(fiatCurrency, CryptoCurrency.NANO.getCode());
                }
            }
        }
        return null;
    }

    @Override
    public ICryptoAddressValidator createAddressValidator(String cryptoCurrency) {
        if (CryptoCurrency.NANO.getCode().equalsIgnoreCase(cryptoCurrency)) {
            return new NanoAddressValidator();
        }
        return null;
    }


    @Override
    public Set<String> getSupportedCryptoCurrencies() {
        Set<String> result = new HashSet<String>();
        result.add(CryptoCurrency.NANO.getCode());
        return result;
    }


    @Override
    public Set<ICryptoCurrencyDefinition> getCryptoCurrencyDefinitions() {
        Set<ICryptoCurrencyDefinition> result = new HashSet<>();
        result.add(DEFINITION);
        return result;
    }


}
