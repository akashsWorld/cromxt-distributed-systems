package com.cromxt.file_handler.clients;


import com.cromxt.file.handler.dtos.requests.BucketRequest;
import com.cromxt.file.handler.dtos.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GatewayClient {

    private final WebClient webClient;


    public Mono<Void> addRoute(BucketRequest bucketRequest){
        return webClient.post()
                .uri("/api/v1/gateway")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bucketRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(ErrorResponse.class)
                        .map(errorResponse -> new RuntimeException(errorResponse.message()))
                )
                .bodyToMono(Void.class)
                .onErrorResume(RuntimeException.class, Mono::error);
    }
}
