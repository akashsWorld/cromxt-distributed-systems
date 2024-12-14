package com.cromxt.bucket_gateway.service;

import com.cromxt.bucket_gateway.client.FileStoreClient;
import com.cromxt.file.handler.dtos.requests.BucketRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@Slf4j
public class DynamicRouteService implements RouteDefinitionLocator {

    private final CopyOnWriteArrayList<RouteDefinition> routeDefinitions;
    private final Boolean isSecure;

    public DynamicRouteService(FileStoreClient fileStoreClient, Boolean isSecure) {
        this.routeDefinitions = new CopyOnWriteArrayList<>();
        this.isSecure = isSecure;

        // Add all the routes to the list getting from file-store server.
        Stream<BucketRequest> bucketRequests = fileStoreClient.getAllAvailableRoutes().toStream();
        bucketRequests.forEach(bucketRequest -> this.routeDefinitions.add(createRouteDefinition(bucketRequest)));
        if(this.routeDefinitions.isEmpty()) log.warn("Gateway starts with 0 buckets");
    }

    public Mono<Void> addRoute(BucketRequest bucketRequest) {
        return Mono.fromRunnable(() -> {
            RouteDefinition routeDefinition = createRouteDefinition(bucketRequest);
            this.routeDefinitions.add(routeDefinition);
        });
    }
    private RouteDefinition createRouteDefinition(BucketRequest bucketRequest) {
            RouteDefinition routeDefinition = new RouteDefinition();

            String requestPath = String.format("/%s/api/v1/medias/**", bucketRequest.id());
            String rewritePathDefinition = String.format("RewritePath=/%s(?<segment>/?.*), $\\{segment}", bucketRequest.id());
            String protocol = isSecure ? "https" : "http";
            String bucketUrl = String.format("%s://%s:%d/api/v1/medias/**",protocol, bucketRequest.hostname(), bucketRequest.port());

//                    ADD predicates to the route.
            routeDefinition.setId(bucketRequest.id());
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
