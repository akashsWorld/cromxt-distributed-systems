package com.cromxt.routeservice.service.impl;

import com.cromxt.file.handler.dtos.requests.BucketObjects;
import com.cromxt.routeservice.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
@Slf4j
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final AvailableRouteDiscovererService availableRouteDiscovererService;

    @Override
    public Flux<BucketObjects> getAllAvailableRoutes() {
        return null;
    }
}
