package com.cromxt.cloudstore.service;

import com.cromxt.dtos.requests.BucketObjects;
import reactor.core.publisher.Flux;

public interface BucketService {
    Flux<BucketObjects> fidAllBuckets();
}
