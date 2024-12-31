package com.cromxt.bucketgateway.service;

import com.cromxt.dtos.requests.BucketObjects;
import reactor.core.publisher.Mono;

public interface RouterService {
    Mono<Void> addRoute(BucketObjects bucketObjects);
    Mono<Void> deleteRoute(String routeId);
    Mono<Void> updateRoute(String routeId, BucketObjects bucketObjects);
}
