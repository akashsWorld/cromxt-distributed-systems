package com.comxt.client.bucket;


import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


public class BucketClient {

    private final WebClient webClient;
    private final String PROTOCOL;

    public BucketClient(WebClient webClient, Environment environment) {
        this.webClient = webClient;
        boolean isSecure = environment.getProperty("BUCKET_SERVER.IS_SECURE", Boolean.class, false);
        this.PROTOCOL =  isSecure ? "https" : "http";
    }

    public Mono<Long> getAvailableSpace(String hostName, Integer port) {
        String url = String.format("%s://%s:%d/api/v1/server/get-available-space", PROTOCOL, hostName, port);
        return webClient.get().uri(url).retrieve().bodyToMono(Map.class).map(body-> Long.valueOf(body.get("availableSpace").toString()))
                .onErrorResume(e -> {
                    System.out.println(e.toString());

                    return Mono.just(0L);
                });
    }
}
