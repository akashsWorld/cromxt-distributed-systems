package com.cromxt.common.requests.client.response;

public record BucketResponseDTO(
        String bucketId,
        String hostname,
        Integer httpPort,
        Integer rpcPort
){
}
