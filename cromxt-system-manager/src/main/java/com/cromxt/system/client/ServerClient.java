package com.cromxt.system.client;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.cromxt.system.client.response.LaunchedInstanceResponse;

import reactor.core.publisher.Mono;

@Service
public class ServerClient {
    private final WebClient webClient;

    public ServerClient(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public Mono<LaunchedInstanceResponse> launchNewBucket(
            String buketId,
            String hostName,
            Integer port) {
        return Mono.empty();
    }


    private String generateUrl(String protocol, String bucketId, String hostName, Integer port) {
        return "http://" + hostName + ":" + port + "/api/v1/bucket/" + bucketId;
    }
}
