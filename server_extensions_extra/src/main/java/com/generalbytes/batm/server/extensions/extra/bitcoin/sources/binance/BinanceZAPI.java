package com.generalbytes.batm.server.extensions.extra.bitcoin.sources.binance;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
@Path("/api/v3")
@Produces(MediaType.APPLICATION_JSON)
public interface BinanceZAPI {
    @GET
    @Path("/ticker/price")
    BinanceZTickerData getTicker(@QueryParam("symbol") String symbol);
}