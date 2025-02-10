package com.cromxt.routeservice.service;


import com.cromxt.common.server.requests.NewBucketRequest;
import reactor.core.publisher.Flux;

public interface RouteService {
    Flux<NewBucketRequest> getAllAvailableRoutes();
}
