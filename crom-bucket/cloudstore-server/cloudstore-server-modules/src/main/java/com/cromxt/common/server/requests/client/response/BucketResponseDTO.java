package com.cromxt.common.server.requests.client.response;

public record BucketResponseDTO(
        String bucketId,
        String hostname,
        Integer httpPort,
        Integer rpcPort
){
}
