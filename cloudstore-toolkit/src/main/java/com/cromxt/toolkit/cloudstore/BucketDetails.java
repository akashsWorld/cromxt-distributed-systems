package com.cromxt.toolkit.cloudstore;

public record BucketDetails(
        String bucketId,
        String hostName,
        Integer httpPort,
        Integer rpcPort
){
}
