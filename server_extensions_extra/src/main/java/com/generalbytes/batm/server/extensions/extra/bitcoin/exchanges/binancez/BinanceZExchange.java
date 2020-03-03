package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.binancez;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.common.currencies.FiatCurrency;
import com.generalbytes.batm.server.extensions.*;
import com.generalbytes.batm.server.extensions.extra.bitcoin.sources.binance.BinanceZRateSource;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.io.IOException;
import java.util.List;

import si.mazi.rescu.RestProxyFactory;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.ClientConfigUtil;
import si.mazi.rescu.HttpStatusIOException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BinanceZExchange implements IExchange {

    private String preferredFiatCurrency = FiatCurrency.ZAR.getCode();
    private String clientKey;
    private String clientSecret;
    private BinanceZExchangeAPI api;
    private BinanceZRateSource bins;
    private final Logger log;

    public BinanceZExchange(String clientKey, String clientSecret, String preferredFiatCurrency) {
        this.preferredFiatCurrency = FiatCurrency.ZAR.getCode();
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
        log = LoggerFactory.getLogger("batm.master.exchange.binancez");
        api = RestProxyFactory.createProxy(BinanceZExchangeAPI.class, "https://api.binance.com");
        bins = new BinanceZRateSource(preferredFiatCurrency);
    }

    public static String sign(String message, String secret) {

        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);

            byte[] bytes = mac.doFinal(message.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();

            for (int i=0; i<bytes.length; i++) {
                String hex = Integer.toHexString(0xFF &  bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to sign message.", e);
        }
    }

    @Override
    public Set<String> getCryptoCurrencies() {
        Set<String> cryptoCurrencies = new HashSet<>();
        cryptoCurrencies.add(CryptoCurrency.DASHD.getCode());
        cryptoCurrencies.add(CryptoCurrency.USDT.getCode());
        return cryptoCurrencies;
    }

    @Override
    public Set<String> getFiatCurrencies() {
        Set<String> fiatCurrencies = new HashSet<>();
        fiatCurrencies.add(FiatCurrency.ZAR.getCode());
        return fiatCurrencies;
    }

    @Override
    public String getPreferredFiatCurrency() {
        return this.preferredFiatCurrency;
    }

    @Override
    public BigDecimal getCryptoBalance(String cryptoCurrency) {
        if (!getCryptoCurrencies().contains(cryptoCurrency)) {
            log.error("Cryptocurrency " + cryptoCurrency + " not supported.");
            return null;
        }
        String crypto = cryptoCurrency;
        if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
            crypto = "DASH";
        }
        try {

            String query = "";
            String timeStamp = String.valueOf(new Date().getTime());
            query = "recvWindow=" + 5000 + "&timestamp=" + timeStamp;

            String signing = sign(query, clientSecret);

            final BinanceZResponse accountInfo = api.getCryptoBalance(this.clientKey, String.valueOf(5000), timeStamp, signing);

            if (accountInfo != null) {
                List<BinanceZAssetData> balances = (List<BinanceZAssetData>) accountInfo.getBalance();
                if(balances != null && !balances.isEmpty()) {
                    for (BinanceZAssetData assetData : balances) {
                        final String asset = (String) assetData.getAsset();
                        BigDecimal value = assetData.getFree();
                        if (asset.equals(crypto)) {
                            log.error("getCryptoBalance {} {}", crypto, value);
                            return value;
                        }
                    }
                }
            }
        } catch (HttpStatusIOException e) {
            log.error(e.getHttpBody());
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public BigDecimal getFiatBalance(String fiatCurrency) {
        final BigDecimal usdtballance = getCryptoBalance("USDT");
        final BigDecimal priceUSDTZAR = bins.getExchangeRateLast("USDT", "ZAR");
        final BigDecimal result = usdtballance.multiply(priceUSDTZAR).setScale(2, BigDecimal.ROUND_CEILING);
        log.error("getDepositAddress {} ", getDepositAddress("DASH"));

        return result;
    }


    @Override
    public String getDepositAddress(String cryptoCurrency) {
        String crypto = cryptoCurrency;
        if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
            crypto = "DASH";
        }
        try {
            String query = "";
            String timeStamp = String.valueOf(new Date().getTime());
            query = "asset=" + crypto + "recvWindow=" + 5000 + "&timestamp=" + timeStamp;

            String signing = sign(query, clientSecret);

            final BinanceZAddressData accountInfo = api.getDepoAddress(this.clientKey, crypto, String.valueOf(5000), timeStamp, signing);
            log.error("getDepositAddress {}", accountInfo.getMsg());
            return accountInfo.getAddress();

        } catch (HttpStatusIOException e) {
            log.error(e.getHttpBody());
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }




    @Override
    public String purchaseCoins(BigDecimal amount, String cryptoCurrency, String fiatCurrencyToUse, String description) {
        return null;
    }


    @Override
    public String sellCoins(BigDecimal cryptoAmount, String cryptoCurrency, String fiatCurrencyToUse, String description) {
        return null;
    }

    @Override
    public String sendCoins(String destinationAddress, BigDecimal amount, String cryptoCurrency, String description) {
        try {
            String crypto = cryptoCurrency;
            if (CryptoCurrency.DASHD.getCode().equalsIgnoreCase(cryptoCurrency)) {
                crypto = "DASH";
            }

            String query = "";
            String timeStamp = String.valueOf(new Date().getTime());
            query = "asset=" + crypto + "&address=" + destinationAddress + "&amount=" + amount + "&name=" + "123" + "&recvWindow=" + 5000 + "&timestamp=" + timeStamp;

            String signing = sign(query, clientSecret);
            BinanceZSendCoinResponse response = api.sendCryptoCurrency(this.clientKey, crypto, destinationAddress, String.valueOf(amount), "123", String.valueOf(5000), timeStamp, signing);

            if (response != null && response.getMsg() != null && response.getSuccess()) {
                return response.getMsg();
            }
        } catch (HttpStatusIOException e) {
            log.error(e.getHttpBody());
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

}
