package com.cromxt.cloudstore.clients;

import com.cromxt.cloudstore.dtos.response.MediaObjectDetails;
import com.cromxt.dtos.response.BucketDetails;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BucketClient {

    Mono<MediaObjectDetails> uploadFile(Flux<DataBuffer> fileData, BucketDetails bucketDetails);
}
