package com.cromxt.bucket_gateway.client.bucket;

import com.cromxt.file.handler.dtos.BucketsResponse;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;

@Service
public class BucketServerClient {

    private final WebClient webClient;
    private final Boolean isSecure;


    public BucketServerClient(WebClient.Builder webClientBuilder, Environment environment) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8550")
                .build();
        this.isSecure = environment.getProperty("BUCKET_GATEWAY.BUCKETS_PROTOCOL", Boolean.class, false);
    }

    public Flux<RouteDefinition> getAllAvailableRoutes() {
            return webClient
                .get()
                .uri("/api/v1/buckets")
                .retrieve()
                .bodyToFlux(BucketsResponse.class)
                .map(bucketsResponse -> {
                    RouteDefinition routeDefinition = new RouteDefinition();

                    String requestPath = String.format("/%s/api/v1/medias/**",bucketsResponse.id());
                    String rewritePathDefinition = String.format("RewritePath=/%s(?<segment>/?.*), $\\{segment}", bucketsResponse.id());
                    String protocol = isSecure ? "https" : "http";
                    String bucketUrl = String.format("%s://%s:%d/api/v1/medias/**",protocol,bucketsResponse.hostname(),bucketsResponse.port());

//                    ADD predicates to the route.
                    routeDefinition.setId(bucketsResponse.id());
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
                });
    }
}
