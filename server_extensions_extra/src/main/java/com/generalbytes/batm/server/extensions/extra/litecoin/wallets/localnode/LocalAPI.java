package com.generalbytes.batm.server.extensions.extra.litecoin.wallets.localnode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface LocalAPI {
    @POST
    @Path("/sendfiattocrypto/{cryptoCurrency}")
    @Consumes(MediaType.APPLICATION_JSON)
    AddressData getAddress(@PathParam("cryptoCurrency") String cryptoCurrency, StatusRequest amount);

    @POST
    @Path("/sendfiattocrypto/{cryptoCurrency}")
    @Consumes(MediaType.APPLICATION_JSON)
    AddressData getAddressWithLabel(@PathParam("cryptoCurrency") String cryptoCurrency, LNAddressRequest request);

    @POST
    @Path("/sendamounttofiat/{cryptoCurrency}")
    @Consumes(MediaType.APPLICATION_JSON)
    BalanceData sendTo(@PathParam("cryptoCurrency") String cryptoCurrency, String address, StatusRequest amount);

    @GET
    @Path("/getstatus/{cryptoCurrency}")
    BalanceData getBalanse(@PathParam("cryptoCurrency") String cryptoCurrency);

    @GET
    @Path("/getstatus/{cryptoCurrency}")
    BalanceData getStatus(@PathParam("cryptoCurrency") String cryptoCurrency, @QueryParam("address") String address);

}
