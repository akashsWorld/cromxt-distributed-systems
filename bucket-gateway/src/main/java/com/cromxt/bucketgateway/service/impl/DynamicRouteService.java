package com.cromxt.bucketgateway.service.impl;

import com.cromxt.bucketgateway.client.BucketServerClient;
import com.cromxt.bucketgateway.service.RouterService;
import com.cromxt.dtos.client.requests.NewBucketRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@Slf4j
public class DynamicRouteService implements RouteDefinitionLocator, RouterService {

    private final List<RouteDefinition> routeDefinitions;
    private final Boolean isSecure;
    private final RouteDefinitionWriter routeDefinitionWriter;
    private final ApplicationEventPublisher applicationEventPublisher;

    public DynamicRouteService(BucketServerClient bucketServerClient,
                               Boolean isSecure,
                               RouteDefinitionWriter routeDefinitionWriter,
                               ApplicationEventPublisher applicationEventPublisher
    ) {
        this.routeDefinitions = new CopyOnWriteArrayList<>();
        this.isSecure = isSecure;
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.applicationEventPublisher = applicationEventPublisher;

        // Add all the routes to the list getting from file-store server.
        Stream<NewBucketRequest> bucketRequests = bucketServerClient.getAllAvailableRoutes().toStream();
        bucketRequests.forEach(bucketRequest -> this.routeDefinitions.add(createRouteDefinition(bucketRequest)));
        if(this.routeDefinitions.isEmpty()) log.warn("Gateway starts with 0 buckets");
    }

    @Override
    public Mono<Void> addRoute(NewBucketRequest newBucketRequest) {
        return routeDefinitionWriter.save(Mono.just(createRouteDefinition(newBucketRequest))).doOnSuccess(routeDefinitions->{
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        });
    }

    @Override
    public Mono<Void> deleteRoute(String routeId) {
        return routeDefinitionWriter.delete(Mono.just(routeId)).doOnSuccess(routeDefinitions->{
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        });
    }

    @Override
    public Mono<Void> updateRoute(String routeId, NewBucketRequest newBucketRequest) {
        return routeDefinitionWriter.delete(Mono.just(routeId)).then(this.addRoute(newBucketRequest));
    }

    private RouteDefinition createRouteDefinition(NewBucketRequest newBucketRequest) {
            RouteDefinition routeDefinition = new RouteDefinition();

            String requestPath = String.format("/%s/api/v1/medias/**", newBucketRequest.getId());
            String rewritePathDefinition = String.format("RewritePath=/%s(?<segment>/?.*), $\\{segment}", newBucketRequest.getId());
            String protocol = isSecure ? "https" : "http";
            String bucketUrl = String.format("%s://%s:%d/api/v1/medias/**",protocol, newBucketRequest.getHostname(), newBucketRequest.getPort());

//                    ADD predicates to the route.
            routeDefinition.setId(newBucketRequest.getId());
            routeDefinition.setPredicates(List.of(
                    new PredicateDefinition("Method=POST,GET"),
                    new PredicateDefinition("Path=" + requestPath)
            ));
//                    ADD filters to the route.
            routeDefinition.setFilters(List.of(
                    new FilterDefinition(rewritePathDefinition)
            ));
            routeDefinition.setUri(URI.create(bucketUrl));
            return routeDefinition;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(this.routeDefinitions);
    }
}
