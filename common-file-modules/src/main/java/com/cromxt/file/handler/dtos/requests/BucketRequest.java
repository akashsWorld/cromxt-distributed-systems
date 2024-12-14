package com.cromxt.file.handler.dtos.requests;

public record BucketRequest(
    String id,
    String hostname,
    Integer port
) {
}
