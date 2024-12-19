package com.cromxt.bucket_gateway.client;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RouteServerClient {

    private final WebClient webClient;


    public RouteServerClient(WebClient.Builder webClientBuilder, Environment environment) {
        String baseUrl = environment.getProperty("BUCKET_GATEWAY.ROUTER_SERVICE_BASE_URL", String.class, "http://localhost:8543");
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<BucketRequest> getAllAvailableRoutes() {
            return webClient
                    .get()
                    .uri("/api/v1/routes")
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Something went wrong")))
                    .bodyToFlux(BucketRequest.class)
                    .onErrorResume(ex-> {
                        log.error("{}",ex.getCause().getMessage());
                        log.warn("Gateway starts with 0 buckets");
                        return Flux.empty();
                    });
    }
}
