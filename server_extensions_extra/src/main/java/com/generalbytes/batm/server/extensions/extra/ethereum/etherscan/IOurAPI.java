package com.generalbytes.batm.server.extensions.extra.ethereum.etherscan;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface IOurAPI {

    @GET
    @Path("/getlabelstatus/{cryptoCurrency}")
    BalanceData getlabelstatus(@PathParam("cryptoCurrency") String cryptoCurrency, @QueryParam("address") String address, @QueryParam("label") String label, @QueryParam("amount") BigDecimal amount);

}
