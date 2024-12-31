package com.cromxt.cloudstore.service;

import com.cromxt.kafka.BucketObjects;
import reactor.core.publisher.Flux;

public interface BucketService {
    Flux<BucketObjects> fidAllBuckets();
}
