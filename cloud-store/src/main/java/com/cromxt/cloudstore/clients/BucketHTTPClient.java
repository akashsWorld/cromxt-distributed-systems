package com.cromxt.cloudstore.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service(value = "bucketHTTPClient")
public class BucketHTTPClient implements BucketClient {

    private final WebClient webClient;

    public BucketHTTPClient(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Override
    public Mono<String> uploadFile(Flux<DataBuffer> fileData, String bucketUrl, String fileId) {

        return fileData
                .collectList()
                .flatMap(dataBuffers -> {
                    MultipartBodyBuilder builder = new MultipartBodyBuilder();
                    builder
                            .asyncPart("file", DataBufferUtils.join(Flux.fromIterable(dataBuffers)), DataBuffer.class)
                            .header("Content-Disposition", "form-data; name=file; filename=" + fileId);

                    return webClient.post()
                            .uri(bucketUrl)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .body(BodyInserters.fromMultipartData(builder.build()))
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, clientResponse -> {
                                log.error("Bucket {} is not available", bucketUrl);
                                return Mono.error(new RuntimeException("Bucket " + bucketUrl + " is not available"));
                            })
                            .bodyToMono(String.class);
                });
    }

}
