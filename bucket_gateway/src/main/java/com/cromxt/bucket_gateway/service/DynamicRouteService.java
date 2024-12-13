package com.cromxt.bucket_gateway.service;

import com.cromxt.bucket_gateway.client.bucket.BucketServerClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DynamicRouteService implements RouteDefinitionLocator {

    private final List<RouteDefinition> routeDefinitions;

    public DynamicRouteService(BucketServerClient bucketServerClient) {
//        TODO: Get all routes from another service.
        this.routeDefinitions = new CopyOnWriteArrayList<>();
        bucketServerClient.getAllAvailableRoutes().subscribe(
                this.routeDefinitions::add
        );
    }

    public void addRoute(RouteDefinition routeDefinition) {
        this.routeDefinitions.add(routeDefinition);
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(this.routeDefinitions);
    }
}
