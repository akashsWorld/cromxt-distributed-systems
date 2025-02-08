package com.cromxt.routeservice.dtos;

public record BucketDetails (
        String bucketId,
        String hostName,
        Integer httpPort,
        Integer rpcPort
){
}
