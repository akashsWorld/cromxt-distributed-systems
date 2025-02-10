package com.cromxt.toolkit.cloudstore;

import com.cromxt.toolkit.cloudstore.response.FileUploadResponse;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveCloudStoreClient {

    Mono<FileUploadResponse> saveFile(MediaObjectDataBuffer mediaObjectDataBuffer);
}
