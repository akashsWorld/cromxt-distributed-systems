package com.cromxt.bucket_gateway.service.impl;

import com.cromxt.bucket_gateway.client.RouteServerClient;
import com.cromxt.bucket_gateway.service.RouterService;
import com.cromxt.file.handler.dtos.requests.BucketRequest;
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

    public DynamicRouteService(RouteServerClient routeServerClient,
                               Boolean isSecure,
                               RouteDefinitionWriter routeDefinitionWriter,
                               ApplicationEventPublisher applicationEventPublisher
    ) {
        this.routeDefinitions = new CopyOnWriteArrayList<>();
        this.isSecure = isSecure;
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.applicationEventPublisher = applicationEventPublisher;

        // Add all the routes to the list getting from file-store server.
        Stream<BucketRequest> bucketRequests = routeServerClient.getAllAvailableRoutes().toStream();
        bucketRequests.forEach(bucketRequest -> this.routeDefinitions.add(createRouteDefinition(bucketRequest)));
        if(this.routeDefinitions.isEmpty()) log.warn("Gateway starts with 0 buckets");
    }

    @Override
    public Mono<Void> addRoute(BucketRequest bucketRequest) {
        return routeDefinitionWriter.save(Mono.just(createRouteDefinition(bucketRequest))).doOnSuccess(routeDefinitions->{
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
    public Mono<Void> updateRoute(String routeId, BucketRequest bucketRequest) {
        return routeDefinitionWriter.delete(Mono.just(routeId)).then(this.addRoute(bucketRequest));
    }

    private RouteDefinition createRouteDefinition(BucketRequest bucketRequest) {
            RouteDefinition routeDefinition = new RouteDefinition();

            String requestPath = String.format("/%s/api/v1/medias/**", bucketRequest.getId());
            String rewritePathDefinition = String.format("RewritePath=/%s(?<segment>/?.*), $\\{segment}", bucketRequest.getId());
            String protocol = isSecure ? "https" : "http";
            String bucketUrl = String.format("%s://%s:%d/api/v1/medias/**",protocol, bucketRequest.getHostname(), bucketRequest.getPort());

//                    ADD predicates to the route.
            routeDefinition.setId(bucketRequest.getId());
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
