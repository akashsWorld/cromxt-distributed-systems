package com.cromxt.system.dtos;

public record SavedBucketRespnse(
        String bucketId,
        String hostname,
        Integer httpPort,
        Integer rpcPort
){
}
