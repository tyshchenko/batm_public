package com.generalbytes.batm.server.extensions.extra.nano;

import com.generalbytes.batm.server.extensions.ICryptoAddressValidator;

public class NanoAddressValidator implements ICryptoAddressValidator {
    @Override
    public boolean isAddressValid(String address) {
        return true;
    }

    @Override
    public boolean isPaperWalletSupported() {
        return false;
    }

    @Override
    public boolean mustBeBase58Address() {
        return false;
    }

}
