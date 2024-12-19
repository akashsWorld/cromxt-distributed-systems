package com.cromxt.bucket_gateway.service;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import reactor.core.publisher.Mono;

public interface RouterService {
    Mono<Void> addRoute(BucketRequest bucketRequest);
    Mono<Void> deleteRoute(String routeId);
    Mono<Void> updateRoute(String routeId, BucketRequest bucketRequest);
}
