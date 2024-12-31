package com.cromxt.bucket_gateway.service;

import com.cromxt.file.handler.dtos.requests.BucketObjects;
import reactor.core.publisher.Mono;

public interface RouterService {
    Mono<Void> addRoute(BucketObjects bucketObjects);
    Mono<Void> deleteRoute(String routeId);
    Mono<Void> updateRoute(String routeId, BucketObjects bucketObjects);
}
