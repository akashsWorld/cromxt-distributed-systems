package com.cromxt.file_handler.service;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import reactor.core.publisher.Mono;

public interface AdminService {
    Mono<String> addBucket(BucketRequest bucketRequest);
}
