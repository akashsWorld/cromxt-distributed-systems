package com.cromxt.bucket.client;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class MediaSeverClient {

    private final WebClient webClient;
    private final String API_KEY;

    public MediaSeverClient(WebClient.Builder webClientBuilder,
                            Environment environment) {
        String mediaClientUrl = environment.getProperty("BUCKET_CONFIG_MEDIA_CLIENT_URL", String.class);

        this.API_KEY = environment.getProperty("BUCKET_CONFIG_API_KEY", String.class);

        assert mediaClientUrl != null;

        this.webClient = WebClient.builder()
                .baseUrl(mediaClientUrl)
                .build();
    }


    public Mono<String> createMediaObject(
            MediaDetails mediaDetails
    ){
        return webClient
                .post()
                .header("Api-Key",API_KEY)
                .bodyValue(mediaDetails)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    log.error("Some error occurred");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(String.class);
    }

    public Mono<Void> updateMediaObject(
            String bucketId,
            UpdateMediaDetails updateMediaDetails
    ){
        return webClient
                .post()
                .header("Api-Key",API_KEY)
                .bodyValue(updateMediaDetails)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    log.error("Some error occurred");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(Void.class);
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class MediaDetails{
        private String bucketId;
        private String fileExtension;
        private String mediaId;
        private String userId;
        private MediaStatus mediaStatus;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class UpdateMediaDetails{
        private Long size;
        private MediaStatus mediaStatus;
    }

    public enum MediaStatus {
        SUCCESS,
        UPLOADING
    }
}
