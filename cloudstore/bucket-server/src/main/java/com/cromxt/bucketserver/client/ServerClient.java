package com.cromxt.bucketserver.client;


import com.cromxt.bucketserver.client.response.LaunchedInstanceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ServerClient {
    private final WebClient webClient;

    public ServerClient(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public Mono<LaunchedInstanceResponse> launchInstance(String protocol,
                                                         String bucketId,
                                                         String hostName,
                                                         Integer port){
        return Mono.empty();
    }



    private String generateUrl(String protocol,String bucketId, String hostName, Integer port){
        return "http://" + hostName + ":" + port + "/api/v1/bucket/" + bucketId;
    }
}
