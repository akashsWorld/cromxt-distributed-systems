package com.cromxt.routeservice.service;


import com.cromxt.common.requests.NewBucketRequest;
import reactor.core.publisher.Flux;

public interface RouteService {
    Flux<NewBucketRequest> getAllAvailableRoutes();
}
