package com.cromxt.toolkit.cloudstore;

import com.cromxt.toolkit.cloudstore.metadata.MediaObjectMetaData;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface ReactiveCloudStoreClient {

    Mono<String> saveFile(Long contentLength, Flux<DataBuffer> data);
}
