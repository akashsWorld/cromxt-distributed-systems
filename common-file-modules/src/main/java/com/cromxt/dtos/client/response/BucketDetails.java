package com.cromxt.dtos.client.response;

public record BucketDetails(
        String bucketId,
        String hostName,
        Integer httpPort,
        Integer rpcPort
){
}
