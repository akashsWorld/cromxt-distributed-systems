package com.cromxt.bucketserver.client;


import com.cromxt.dtos.client.requests.NewBucketRequest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BucketGatewayClient {

    private final WebClient webClient;

    public BucketGatewayClient(WebClient.Builder webClientBuilder, Environment environment) {
        String baseUrl = environment.getProperty("BUCKET_GATEWAY_BASE_URL", String.class, "http://localhost:8543");
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<Void> updateBucketsInGateway(NewBucketRequest newBucketRequest) {
        return webClient.post()
                .uri("/api/v1/gateway")
                .bodyValue(newBucketRequest)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
