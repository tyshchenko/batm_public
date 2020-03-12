package com.generalbytes.batm.server.extensions.extra.nano.wallets.nano;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface NanoAPI {
    @GET
    @Path("/getaddress")
    AddressData getAddress();

    @GET
    @Path("/genaddress")
    AddressData getNewAddress();

    @GET
    @Path("/getbalance")
    BalanceData getBalanse(@QueryParam("address") String address);

}
