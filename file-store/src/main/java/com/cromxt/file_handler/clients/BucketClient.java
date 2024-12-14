package com.cromxt.file_handler.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BucketClient {

    private final WebClient webClient;



    public Mono<String> uploadFile(FilePart filePart, String bucketId) {
        String url = String.format("%s/api/v1/medias/upload", bucketId);
        return filePart
                .content()
                .collectList()
                .flatMap(dataBuffers -> {
                    MultipartBodyBuilder builder = new MultipartBodyBuilder();
                    builder
                            .asyncPart("file", DataBufferUtils.join(Flux.fromIterable(dataBuffers)), DataBuffer.class)
                            .header("Content-Disposition", "form-data; name=file; filename=" + filePart.filename());

                    return webClient.post()
                            .uri(url)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .body(BodyInserters.fromMultipartData(builder.build()))
                            .retrieve()
                            .bodyToMono(String.class);
                });
    }
}
