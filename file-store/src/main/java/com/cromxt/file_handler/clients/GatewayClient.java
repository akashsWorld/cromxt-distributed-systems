package com.cromxt.file_handler.clients;


import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GatewayClient {

    private final WebClient webClient;


    public Mono<String> addServer(){
        return null;
    }
}
