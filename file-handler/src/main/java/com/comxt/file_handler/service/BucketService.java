package com.comxt.file_handler.service;

import com.comxt.file_handler.dtos.request.AddBucketRequest;
import reactor.core.publisher.Mono;

public interface BucketService {

    Mono<Void> addBucket(AddBucketRequest addBucketRequest);
}
