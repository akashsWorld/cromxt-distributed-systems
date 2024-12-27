package com.cromxt.route_service.service;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import reactor.core.publisher.Flux;

public interface RouteService {

    Flux<BucketRequest> getAllAvailableRoutes();

}
