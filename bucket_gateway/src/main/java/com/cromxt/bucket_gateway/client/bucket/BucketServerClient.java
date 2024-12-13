package com.cromxt.bucket_gateway.client.bucket;

import com.cromxt.file.handler.dtos.BucketsResponse;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;

@Service
public class BucketServerClient {

    private final WebClient webClient;


    public BucketServerClient(WebClient.Builder webClientBuilder) {

        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8550")
                .build();
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
                    System.out.println(rewritePathDefinition);
                    routeDefinition.setId(bucketsResponse.id());
                    routeDefinition.setPredicates(List.of(
                            new PredicateDefinition("Method=POST"),
                            new PredicateDefinition("Path=" + requestPath)
                    ));
                    routeDefinition.setFilters(List.of(
                            new FilterDefinition(rewritePathDefinition)
                    ));
                    routeDefinition.setUri(URI.create("http://127.0.0.1:8090/api/v1/medias/**"));
                    return routeDefinition;
                });
    }
}
