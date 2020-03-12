package com.generalbytes.batm.server.extensions.extra.nano.wallets.nano;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressData {

    @JsonProperty("address")
    private String address;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
