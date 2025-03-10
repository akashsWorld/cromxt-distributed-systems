package com.cromxt.bucketgateway.client;


import com.cromxt.dtos.client.requests.FileMetaData;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RouteServiceClient {
    private final WebClient webClient;

    public RouteServiceClient(WebClient.Builder webClientBuilder, Environment environment) {
        String baseUrl = environment.getProperty("GATEWAY_CONFIG_ROUTE_SERVICE_URL", String.class);
        assert baseUrl != null;
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<String> getBucketId(FileMetaData fileMetaData) {
        return webClient
                .post()
                .uri("/api/v1/route")
                .bodyValue(fileMetaData)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Something went wrong")))
                .bodyToMono(String.class);
    }
}
