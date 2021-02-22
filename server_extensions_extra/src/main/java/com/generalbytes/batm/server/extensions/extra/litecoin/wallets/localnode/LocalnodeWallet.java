package com.generalbytes.batm.server.extensions.extra.litecoin.wallets.localnode;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.IWallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import si.mazi.rescu.RestProxyFactory;

public class LocalnodeWallet implements IWallet{
    private String cryptoCurrency = CryptoCurrency.LTC.getCode();
    private static final Logger log = LoggerFactory.getLogger(NanoWallet.class);
    private LocalAPI api;

    public LocalnodeWallet(String preferedFiatCurrency, String preferedCryptoCurrency) {
        api = RestProxyFactory.createProxy(LocalAPI.class, "http://127.0.0.1:8099/");
    }

    @Override
    public Set<String> getCryptoCurrencies() {
        Set<String> currencies = new HashSet<>();
        cryptoCurrencies.add(CryptoCurrency.ETH.getCode());
        cryptoCurrencies.add(CryptoCurrency.LTC.getCode());
        cryptoCurrencies.add(CryptoCurrency.DASHD.getCode());
        return currencies;
    }

    @Override
    public String getPreferredCryptoCurrency() {
        return cryptoCurrency;
    }

    @Override
    public String getCryptoAddress(String cryptoCurrency) {
        if (!getCryptoCurrencies().contains(cryptoCurrency)) {
            log.error("wallet error: unknown cryptocurrency.");
            return null;
        }
        final AddressData address = api.getAddress();
        return address.getAddress();
    }

    public BalanceData getStatus(String address) {
        if (!getCryptoCurrencies().contains(cryptoCurrency)) {
            log.error("wallet error: unknown cryptocurrency.");
            return null;
        }
        final BalanceData balance = api.getBalanse(address);
        return balance;
    }

    public String getNewCryptoAddress(String cryptoCurrency) {
        if (!getCryptoCurrencies().contains(cryptoCurrency)) {
            log.error("wallet error: unknown cryptocurrency.");
            return null;
        }
        final AddressData address = api.getNewAddress();
        return address.getAddress();
    }

    @Override
    public BigDecimal getCryptoBalance(String cryptoCurrency) {
        if (!getCryptoCurrencies().contains(cryptoCurrency)) {
            log.error("wallet error: unknown cryptocurrency.");
            return null;
        }
        BigDecimal balance = BigDecimal.ZERO;
        return balance;
    }

    @Override
    public String sendCoins(String destinationAddress, BigDecimal amount, String cryptoCurrency, String description) {
        if (!getCryptoCurrencies().contains(cryptoCurrency)) {
            log.error("wallet error: unknown cryptocurrency.");
            return null;
        }

        return "SendcoinOk";
    }

}
