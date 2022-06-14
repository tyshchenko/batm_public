package com.generalbytes.batm.server.extensions.extra.litecoin.wallets.localnode;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.IWallet;
import com.generalbytes.batm.server.extensions.IGeneratesNewDepositCryptoAddress;
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

public class LocalnodeWallet implements IWallet, IGeneratesNewDepositCryptoAddress{
    private String cryptoCurrency = CryptoCurrency.LTC.getCode();
    private static final Logger log = LoggerFactory.getLogger(LocalnodeWallet.class);
    private LocalAPI api;

    public LocalnodeWallet(String preferedFiatCurrency, String preferedCryptoCurrency) {
        api = RestProxyFactory.createProxy(LocalAPI.class, "http://127.0.0.1:8099/");
    }

    @Override
    public Set<String> getCryptoCurrencies() {
        Set<String> cryptoCurrencies = new HashSet<>();
        cryptoCurrencies.add(CryptoCurrency.ETH.getCode());
        cryptoCurrencies.add(CryptoCurrency.LTC.getCode());
        cryptoCurrencies.add(CryptoCurrency.XRP.getCode());
        cryptoCurrencies.add(CryptoCurrency.DASHD.getCode());
        cryptoCurrencies.add(CryptoCurrency.DOGE.getCode());
        cryptoCurrencies.add(CryptoCurrency.SHIB.getCode());
        cryptoCurrencies.add(CryptoCurrency.SOL.getCode());
        cryptoCurrencies.add(CryptoCurrency.BNBBSC.getCode());
        cryptoCurrencies.add(CryptoCurrency.USDC.getCode());
        return cryptoCurrencies;
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
        String rcryptoCurrency = cryptoCurrency;
        if (CryptoCurrency.BNBBSC.getCode().equalsIgnoreCase(cryptoCurrency)) {
            rcryptoCurrency = "BNB";
        }
        if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
            rcryptoCurrency = "DASH";
        }
        StatusRequest amount = new StatusRequest();
        amount.setAmount(new BigDecimal(1));
        final AddressData address = api.getAddress(rcryptoCurrency, amount);
        return address.getAddress();
    }

    public BalanceData getStatus(String address, String cryptoCurrency) {
        if (!getCryptoCurrencies().contains(cryptoCurrency)) {
            log.error("wallet error: unknown cryptocurrency.");
            return null;
        }
        String rcryptoCurrency = cryptoCurrency;
        if (CryptoCurrency.BNBBSC.getCode().equalsIgnoreCase(cryptoCurrency)) {
            rcryptoCurrency = "BNB";
        }
        if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
            rcryptoCurrency = "DASH";
        }
        final BalanceData balance = api.getBalanse(rcryptoCurrency, address);
        return balance;
    }

    public String getNewCryptoAddress(String cryptoCurrency) {
        if (!getCryptoCurrencies().contains(cryptoCurrency)) {
            log.error("wallet error: unknown cryptocurrency.");
            return null;
        }
        StatusRequest amount = new StatusRequest();
        amount.setAmount(new BigDecimal(1));
        String rcryptoCurrency = cryptoCurrency;
        if (CryptoCurrency.BNBBSC.getCode().equalsIgnoreCase(cryptoCurrency)) {
            rcryptoCurrency = "BNB";
        }
        if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
            rcryptoCurrency = "DASH";
        }
        final AddressData address = api.getAddress(rcryptoCurrency, amount);
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

    @Override
    public String generateNewDepositCryptoAddress(String cryptoCurrency, String label) {
        return getCryptoAddress(cryptoCurrency);
    }

}
