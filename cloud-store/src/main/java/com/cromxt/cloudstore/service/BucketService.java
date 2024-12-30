package com.cromxt.cloudstore.service;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import reactor.core.publisher.Flux;

public interface BucketService {
    Flux<BucketRequest> fidAllBuckets();
}
