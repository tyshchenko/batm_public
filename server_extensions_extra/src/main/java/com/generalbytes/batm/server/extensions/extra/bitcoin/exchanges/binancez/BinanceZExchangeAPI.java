package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.binancez;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface BinanceZExchangeAPI {
    @GET
    @Path("/api/v3/account")
    BinanceZResponse getCryptoBalance(@HeaderParam("X-MBX-APIKEY") String apiKey, @QueryParam("recvWindow") String recvWindow, @QueryParam("timestamp") String timeStamp, @QueryParam("signature") String signature ) throws IOException;

    @POST
    @Path("/sapi/v1/capital/withdraw/apply")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    BinanceZSendCoinResponse sendCryptoCurrency(@HeaderParam("X-MBX-APIKEY") String apiKey, @QueryParam("coin") String coin, @QueryParam("address") String address, @QueryParam("amount") String amount, @QueryParam("name") String name, @QueryParam("recvWindow") String recvWindow, @QueryParam("timestamp") String timeStamp, @QueryParam("signature") String signature) throws IOException;

    @GET
    @Path("/sapi/v1/capital/deposit/address")
    BinanceZAddressData getDepoAddress(@HeaderParam("X-MBX-APIKEY") String apiKey, @QueryParam("coin") String coin, @QueryParam("recvWindow") String recvWindow, @QueryParam("timestamp") String timeStamp, @QueryParam("signature") String signature) throws IOException;

    @POST
    @Path("/api/v3/order")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    BinanceZOrderData market(@HeaderParam("X-MBX-APIKEY") String apiKey, @QueryParam("symbol") String symbol, @QueryParam("side") String side, @QueryParam("quantity") String quantity, @QueryParam("type") String type, @QueryParam("recvWindow") String recvWindow, @QueryParam("timestamp") String timeStamp, @QueryParam("signature") String signature) throws IOException;

}
