package com.cromxt.routeservice.service;

import com.cromxt.dtos.requests.BucketObjects;
import reactor.core.publisher.Flux;

public interface RouteService {

    Flux<BucketObjects> getAllAvailableRoutes();

}
