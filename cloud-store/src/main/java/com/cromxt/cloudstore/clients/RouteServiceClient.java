package com.cromxt.cloudstore.clients;


import com.cromxt.dtos.requests.BucketObjects;
import com.cromxt.dtos.requests.FileMetaData;
import com.cromxt.dtos.response.BucketResponse;
import com.cromxt.dtos.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RouteServiceClient {

    private final WebClient webClient;

    public RouteServiceClient(WebClient.Builder webClient, Environment environment) {
        String baseUrl = environment.getProperty("CLOUD_STORE_CONFIG_ROUTE_SERVICE_URL", String.class);
        assert baseUrl != null;
        this.webClient = webClient
                .baseUrl(baseUrl)
                .build();
    }
    public Mono<BucketResponse> getBucketId(FileMetaData fileMetaData) {
        return webClient
                .post()
                .uri("/api/v1/routing/get-bucket-id")
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .bodyValue(fileMetaData)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException(clientResponse.toString())))
                .bodyToMono(BucketResponse.class);
    }
}
