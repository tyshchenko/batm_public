package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.common.currencies.FiatCurrency;
import com.generalbytes.batm.server.extensions.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Iterator;

import si.mazi.rescu.RestProxyFactory;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.ClientConfigUtil;
import si.mazi.rescu.HttpStatusIOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;

public class ValrExchange implements IExchange {

    private String preferredFiatCurrency = FiatCurrency.ZAR.getCode();
    private String clientKey;
    private String clientSecret;
    private String typeorder;
    private ValrExchangeAPI api;
    private final Logger log;

    public ValrExchange(String clientKey, String clientSecret, String preferredFiatCurrency, String typeorder) {
        this.preferredFiatCurrency = FiatCurrency.ZAR.getCode();
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
        this.typeorder = typeorder;
        log = LoggerFactory.getLogger("batm.master.exchange.valr");

        api = RestProxyFactory.createProxy(ValrExchangeAPI.class, "https://api.valr.com");
    }


    @Override
    public Set<String> getCryptoCurrencies() {
        Set<String> cryptoCurrencies = new HashSet<>();
        cryptoCurrencies.add(CryptoCurrency.BTC.getCode());
        cryptoCurrencies.add(CryptoCurrency.ETH.getCode());
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

    public static String signRequest(String apiKeySecret, String timestamp, String verb, String path, String body) {
        try {
            Mac hmacSHA512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(apiKeySecret.getBytes(), "HmacSHA512");
            hmacSHA512.init(secretKeySpec);
            hmacSHA512.update(timestamp.getBytes());
            hmacSHA512.update(verb.toUpperCase().getBytes());
            hmacSHA512.update(path.getBytes());
            hmacSHA512.update(body.getBytes());
            byte[] digest = hmacSHA512.doFinal();

            return toHexString(digest);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Unable to sign request", e);
        }
    }

    public static String toHexString(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public BigDecimal getBalance(String symbol, List<ValrBalances> balance) {
        for (Iterator<ValrBalances> i = balance.iterator(); i.hasNext();) {
            ValrBalances item = i.next();
            if (item.getCurrency().equals(symbol)) {
                log.debug("{} balance = {}", item.getCurrency(), item.getBalance());
                return item.getBalance();
            }
        }
        return new BigDecimal("0.0");
    }

    @Override
    public String getDepositAddress(String cryptoCurrency) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = signRequest(clientSecret, timestamp, "GET", "/v1/wallet/crypto/"+cryptoCurrency+"/deposit/address", "");
        final ValrAddressData address = api.getAddress(cryptoCurrency, clientKey, signature, timestamp);
        return address.getAddress();
    }


    @Override
    public BigDecimal getFiatBalance(String fiatCurrency) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = signRequest(clientSecret, timestamp, "GET", "/v1/account/balances", "");
        try {
            final List<ValrBalances> balance = api.getBalance(clientKey, signature, timestamp);
            final BigDecimal fiatballance = getBalance("ZAR",balance);
            log.debug("{} exbalance = {}", fiatCurrency, fiatballance);
            return fiatballance;
        } catch (HttpStatusIOException e) {
            log.error("Error {}", e.getHttpBody());
            return null;
        }
    }

    @Override
    public BigDecimal getCryptoBalance(String cryptoCurrency) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = signRequest(clientSecret, timestamp, "GET", "/v1/account/balances", "");
        try {
            final List<ValrBalances> balance = api.getBalance(clientKey, signature, timestamp);
            BigDecimal cryptoballance;
            cryptoballance = getBalance(cryptoCurrency,balance);
            log.debug("{} exbalance = {}", cryptoCurrency, cryptoballance);
            return cryptoballance;
        } catch (HttpStatusIOException e) {
            log.error("Error {}", e.getHttpBody());
            return null;
        }
    }

    @Override
    public String sendCoins(String destinationAddress, BigDecimal amount, String cryptoCurrency, String description) {
        try {
            Thread.sleep(2000); //give exchange 2 seconds to reflect open order in order book
        } catch (InterruptedException e) {
            log.error("Error", e);
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = signRequest(clientSecret, timestamp, "POST", "/v1/wallet/crypto/"+cryptoCurrency+"/withdraw", "{\"address\":\""+destinationAddress+"\",\"amount\":\""+amount.toString()+"\"}");
        ValrSend senddata;
        senddata.setAddress(destinationAddress);
        senddata.setAmount(amount.toString());
        final ValrRequestData result = api.sendMoney(senddata, cryptoCurrency, clientKey, signature, timestamp);
        return result.getResult();
    }

    @Override
    public String purchaseCoins(BigDecimal amount, String cryptoCurrency, String fiatCurrencyToUse, String description) {
        String type = "BUY";
        String pair;
        pair = cryptoCurrency.toUpperCase() + "ZAR";

        final ValrTickerData cryptoToZar = api.getTicker(pair);
        BigDecimal pricebid  = cryptoToZar.getBid();
        BigDecimal one       = new BigDecimal(1);
        BigDecimal onepr     = new BigDecimal(1.01);
        BigDecimal cryptofee    = new BigDecimal(0.00033);
        amount               = amount.multiply(onepr);
        amount               = amount.add(cryptofee).setScale(6, BigDecimal.ROUND_CEILING);
        BigDecimal price     = pricebid.add(one).setScale(0, BigDecimal.ROUND_CEILING);
        BigDecimal amountincrypto = price.multiply(amount).setScale(2, BigDecimal.ROUND_CEILING);
        String timestamp = String.valueOf(System.currentTimeMillis());

        if (this.typeorder.equals("limit")) {
            String signature = signRequest(clientSecret, timestamp, "POST", "/v1/orders/limit", "{\"side\":\""+type+"\",\"quantity\":\""+amount.toString()+"\",\"price\":\""+price.toString()+"\",\"pair\":\""+pair+"\"}");

            log.debug("limit pair {} type {} amount {} price {}", pair, type, amount.toString(), price.toString());
            ValrLimitBuyOrder buyOrder;
            buyOrder.setPair(pair);
            buyOrder.setSide(type);
            buyOrder.setAmount(amount.toString());
            buyOrder.setPrice(price.toString());
            final ValrOrderData result = api.createLimitBuyOrder(buyOrder, clientKey, signature, timestamp);
            return result.getResult();
        } else {
            String signature = signRequest(clientSecret, timestamp, "POST", "/v1/orders/market", "{\"side\":\""+type+"\",\"quoteAmount\":\""+amountincrypto.toString()+"\",\"pair\":\""+pair+"\"}");

            log.debug("market pair {} type {} amount   {}  ", pair, type, amountincrypto.toString());
            ValrBuyOrder buyOrder;
            buyOrder.setPair(pair);
            buyOrder.setSide(type);
            buyOrder.setAmount(amountincrypto.toString());
            final ValrOrderData result = api.createBuyOrder(buyOrder, clientKey, signature, timestamp);
            return result.getResult();
        }
    }


    @Override
    public String sellCoins(BigDecimal cryptoAmount, String cryptoCurrency, String fiatCurrencyToUse, String description) {
        String type = "SELL";
        String pair;
        pair = cryptoCurrency.toUpperCase() + "ZAR";
        String timestamp = String.valueOf(System.currentTimeMillis());

        if (this.typeorder.equals("limit")) {
            final ValrTickerData cryptoToZar = api.getTicker(pair);
            BigDecimal priceask  = cryptoToZar.getAsk();
            BigDecimal one       = new BigDecimal(1);
            BigDecimal price     = priceask.subtract(one).setScale(0, BigDecimal.ROUND_CEILING);

            String signature = signRequest(clientSecret, timestamp, "POST", "/v1/orders/limit", "{\"side\":\""+type+"\",\"quantity\":\""+cryptoAmount.toString()+"\",\"price\":\""+price.toString()+"\",\"pair\":\""+pair+"\"}");
            ValrLimitSellOrder sellOrder;
            sellOrder.setPair(pair);
            sellOrder.setSide(type);
            sellOrder.setAmount(cryptoAmount.toString());
            sellOrder.setPrice(price.toString());

            final ValrOrderData result = api.createLimitSellOrder(sellOrder, clientKey, signature, timestamp);
            log.debug("limit pair {} type {} amount {} price {} result {}", pair, "ASK", cryptoAmount.toString(), price.toString(), result.getResult());
            return result.getResult();
        } else {
            String signature = signRequest(clientSecret, timestamp, "POST", "/v1/orders/market", "{\"side\":\""+type+"\",\"baseAmount\":\""+cryptoAmount.toString()+"\",\"pair\":\""+pair+"\"}");
            ValrSellOrder sellOrder;
            sellOrder.setPair(pair);
            sellOrder.setSide(type);
            sellOrder.setAmount(cryptoAmount.toString());

            final ValrOrderData result = api.createSellOrder(sellOrder, clientKey, signature, timestamp);
            log.debug("market pair {} type {} amount   {}   result {}", pair, type, cryptoAmount.toString(), result.getResult());
            return result.getResult();
        }
    }
}
