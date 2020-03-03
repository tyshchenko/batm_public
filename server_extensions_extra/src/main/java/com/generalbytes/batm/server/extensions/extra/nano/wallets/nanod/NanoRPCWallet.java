package com.generalbytes.batm.server.extensions.extra.nano.wallets.nanod;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.extra.common.RPCWallet;


public class NanoRPCWallet extends RPCWallet {
    public NanoRPCWallet(String rpcURL, String accountName) {
        super(rpcURL, accountName, CryptoCurrency.NANO.getCode());
    }
}
