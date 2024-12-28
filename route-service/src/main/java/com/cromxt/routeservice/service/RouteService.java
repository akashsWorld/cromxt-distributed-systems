package com.cromxt.routeservice.service;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import reactor.core.publisher.Flux;

public interface RouteService {

    Flux<BucketRequest> getAllAvailableRoutes();

}
