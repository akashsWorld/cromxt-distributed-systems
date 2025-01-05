package com.cromxt.bucketgateway.service;

import com.cromxt.dtos.client.requests.NewBucketRequest;
import reactor.core.publisher.Mono;

public interface RouterService {
    Mono<Void> addRoute(NewBucketRequest newBucketRequest);
    Mono<Void> deleteRoute(String routeId);
    Mono<Void> updateRoute(String routeId, NewBucketRequest newBucketRequest);
}
