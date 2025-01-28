package com.cromxt.bucketserver.exception;

public class InvalidBucketDetails extends RuntimeException {
    public InvalidBucketDetails(String message) {
        super(message);
    }
}
