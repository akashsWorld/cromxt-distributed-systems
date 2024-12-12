package com.comxt.file_handler.dtos.request;

public record AddBucketRequest(
        String bucketName,
        String bucketHostName,
        Integer bucketPort
) {
}
