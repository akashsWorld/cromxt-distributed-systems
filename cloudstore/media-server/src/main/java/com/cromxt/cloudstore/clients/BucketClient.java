package com.cromxt.cloudstore.clients;

import com.cromxt.cloudstore.dtos.MediaObjectMetadata;
import com.cromxt.cloudstore.dtos.response.MediaObjectDetails;
import com.cromxt.dtos.client.response.BucketAddress;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BucketClient {

    Mono<MediaObjectDetails> uploadFile(Flux<DataBuffer> fileData,
                                        MediaObjectMetadata mediaObjectDetails,
                                        BucketAddress bucketAddress);
}
