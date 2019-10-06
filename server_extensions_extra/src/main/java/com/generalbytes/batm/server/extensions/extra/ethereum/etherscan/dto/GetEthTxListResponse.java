package com.generalbytes.batm.server.extensions.extra.ethereum.etherscan.dto;

import java.util.List;

public class GetEthTxListResponse {
    public String status;
    public String message;
    public List<EthTransaction> result;


    public static class EthTransaction {
         public String value;
         public String blockNumber;
         public String confirmations;
         public String contractAddress;
         public String timestamp;
         public String hash;
         public String blockHash;
         public String from;
         public String to;
         public String isError;
    }
}
