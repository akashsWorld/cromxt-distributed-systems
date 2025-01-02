package com.cromxt.cloudstore.clients;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BucketClient {

    Mono<String> uploadFile(Flux<DataBuffer> fileData, String bucketUrl, String fileId);
}
