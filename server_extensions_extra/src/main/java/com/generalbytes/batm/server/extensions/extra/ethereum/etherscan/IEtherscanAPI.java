package com.generalbytes.batm.server.extensions.extra.ethereum.etherscan;

import com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.dto.GetTokenTransactionsResponse;
import com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.dto.GetEthBalanceResponse;
import com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.dto.GetEthTxListResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface IEtherscanAPI {

    @GET
    @Path("/api")
    GetTokenTransactionsResponse getTokenTransactions(@QueryParam("module") String module, @QueryParam("action") String action, @QueryParam("address") String address);

    @GET
    @Path("/api")
    GetEthBalanceResponse getEthBalance(@QueryParam("module") String module, @QueryParam("action") String action, @QueryParam("address") String address);

    @GET
    @Path("/api")
    GetEthTxListResponse getEthTxList(@QueryParam("module") String module, @QueryParam("action") String action, @QueryParam("address") String address);

}
