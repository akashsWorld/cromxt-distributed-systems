package com.cromxt.routeservice.client;


import com.cromxt.dtos.requests.BucketObjects;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BucketServerClient {

    private final WebClient webClient;

    public BucketServerClient(WebClient.Builder webClient, Environment environment) {
        String bucketServerUrl = environment.getProperty("BUCKET_SERVER_URL", String.class, "http://localhost:8080");

        if (bucketServerUrl.endsWith("/")) {
            bucketServerUrl = bucketServerUrl.substring(0, bucketServerUrl.length() - 1);
        }
        this.webClient = webClient
                .baseUrl(bucketServerUrl)
                .build();
    }

    public Flux<BucketObjects> getAllBuckets() {
        return webClient
                .get()
                .uri("/api/v1/buckets")
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Something went wrong")))
                .bodyToFlux(BucketObjects.class);
    }


}
