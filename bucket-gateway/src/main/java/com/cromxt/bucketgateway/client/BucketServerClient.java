package com.cromxt.bucketgateway.client;

import com.cromxt.dtos.client.requests.NewBucketRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BucketServerClient {

    private final WebClient webClient;


    public BucketServerClient(WebClient.Builder webClientBuilder, Environment environment) {
        String baseUrl = environment.getProperty("GATEWAY_CONFIG_BUCKETS_SERVER_URL", String.class, "localhost:8543");
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<NewBucketRequest> getAllAvailableRoutes() {
            return webClient
                    .get()
                    .uri("/api/v1/routes")
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Something went wrong")))
                    .bodyToFlux(NewBucketRequest.class)
                    .onErrorResume(ex-> {
                        log.error("{}",ex.getCause().getMessage());
                        log.warn("Gateway starts with 0 buckets");
                        return Flux.empty();
                    });
    }
}
