package com.cromxt.routeservice.service;

import com.cromxt.kafka.BucketObjects;
import reactor.core.publisher.Flux;

public interface RouteService {

    Flux<BucketObjects> getAllAvailableRoutes();

}
