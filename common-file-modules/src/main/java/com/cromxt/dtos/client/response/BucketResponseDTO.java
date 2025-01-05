package com.cromxt.dtos.client.response;

public record BucketResponseDTO(
        String bucketId,
        String hostname,
        Integer port
){
}
