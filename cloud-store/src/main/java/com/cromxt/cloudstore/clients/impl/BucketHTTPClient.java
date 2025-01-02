package com.cromxt.cloudstore.clients.impl;

import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.cloudstore.dtos.response.MediaObjectDetails;
import com.cromxt.dtos.response.BucketDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class BucketHTTPClient implements BucketClient {

    private final WebClient webClient;

    public BucketHTTPClient(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Override
    public Mono<MediaObjectDetails> uploadFile(Flux<DataBuffer> fileData, BucketDetails bucketDetails) {

        String url = generateUrl(bucketDetails);
        return fileData
                .collectList()
                .flatMap(dataBuffers -> {
                    MultipartBodyBuilder builder = new MultipartBodyBuilder();
                    builder
                            .asyncPart("file", DataBufferUtils.join(Flux.fromIterable(dataBuffers)), DataBuffer.class)
                            .header("Content-Disposition", "form-data; name=file; filename=" + "file");

                    return webClient.post()
                            .uri(url)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .body(BodyInserters.fromMultipartData(builder.build()))
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, clientResponse -> {
                                log.error("Bucket {} is not available", url);
                                return Mono.error(new RuntimeException("Bucket " + url + " is not available"));
                            })
                            .bodyToMono(String.class);
                })
                .flatMap(fileResponse->
//                        TODO: Handle the Response.
                        Mono.empty());
    }

    private String generateUrl(BucketDetails bucketDetails) {
        return String.format("http://%s:%s", bucketDetails.url(), bucketDetails.port());
    }

}
