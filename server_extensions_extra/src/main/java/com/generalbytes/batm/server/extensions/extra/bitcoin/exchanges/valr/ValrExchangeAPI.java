/*************************************************************************************
 * Copyright (C) 2014-2018 GENERAL BYTES s.r.o. All rights reserved.
 *
 * This software may be distributed and modified under the terms of the GNU
 * General Public License version 2 (GPL2) as published by the Free Software
 * Foundation and appearing in the file GPL2.TXT included in the packaging of
 * this file. Please note that GPL2 Section 2[b] requires that all works based
 * on this software must also be made publicly available under the terms of
 * the GPL2 ("Copyleft").
 *
 * Contact information
 * -------------------
 *
 * GENERAL BYTES s.r.o.
 * Web      :  http://www.generalbytes.com
 *
 ************************************************************************************/
package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.valr;

import java.math.BigDecimal;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import si.mazi.rescu.HttpStatusIOException;

@Path("v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ValrExchangeAPI {
    @GET
    @Path("/account/balances")
    List<ValrBalances> getBalance(@HeaderParam("X-VALR-API-KEY") String apiKey, @HeaderParam("X-VALR-SIGNATURE") String signature, @HeaderParam("X-VALR-TIMESTAMP") String timestamp) throws HttpStatusIOException;

    @GET
    @Path("/account/balances")
    String getBalanceTest(@HeaderParam("X-VALR-API-KEY") String apiKey, @HeaderParam("X-VALR-SIGNATURE") String signature, @HeaderParam("X-VALR-TIMESTAMP") String timestamp);

    @GET
    @Path("/wallet/crypto/{symbol}/deposit/address")
    ValrAddressData getAddress(@PathParam("symbol") String symbol, @HeaderParam("X-VALR-API-KEY") String apiKey, @HeaderParam("X-VALR-SIGNATURE") String signature, @HeaderParam("X-VALR-TIMESTAMP") String timestamp);

    @POST
    @Path("/wallet/crypto/{symbol}/withdraw")
    ValrRequestData sendMoney(@FormParam("address") String destinationAddress, @FormParam("amount") String amount, @PathParam("symbol") String cryptoCurrency, @HeaderParam("X-VALR-API-KEY") String apiKey, @HeaderParam("X-VALR-SIGNATURE") String signature, @HeaderParam("X-VALR-TIMESTAMP") String timestamp);

    @POST
    @Path("/orders/market")
    ValrOrderData createBuyOrder(@FormParam("pair") String pair, @FormParam("side") String type, @FormParam("quoteAmount") String volume, @HeaderParam("X-VALR-API-KEY") String apiKey, @HeaderParam("X-VALR-SIGNATURE") String signature, @HeaderParam("X-VALR-TIMESTAMP") String timestamp);

    @POST
    @Path("/orders/market")
    ValrOrderData createSellOrder(@FormParam("pair") String pair, @FormParam("side") String type, @FormParam("baseAmount") String volume, @HeaderParam("X-VALR-API-KEY") String apiKey, @HeaderParam("X-VALR-SIGNATURE") String signature, @HeaderParam("X-VALR-TIMESTAMP") String timestamp);

    @POST
    @Path("/orders/limit")
    ValrOrderData createLimitBuyOrder(@FormParam("pair") String pair, @FormParam("side") String type, @FormParam("quantity") String volume, @FormParam("price") String price, @HeaderParam("X-VALR-API-KEY") String apiKey, @HeaderParam("X-VALR-SIGNATURE") String signature, @HeaderParam("X-VALR-TIMESTAMP") String timestamp);

    @POST
    @Path("/orders/limit")
    ValrOrderData createLimitSellOrder(@FormParam("pair") String pair, @FormParam("side") String type, @FormParam("volume") String volume, @FormParam("price") String price, @HeaderParam("X-VALR-API-KEY") String apiKey, @HeaderParam("X-VALR-SIGNATURE") String signature, @HeaderParam("X-VALR-TIMESTAMP") String timestamp);

    @GET
    @Path("/{pair}/marketsummary")
    ValrTickerData getTicker(@PathParam("pair") String symbol);

}
