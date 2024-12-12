package com.cromxt.crom_bucket.service;

import com.cromxt.crom_bucket.dtos.request.MediaUploadRequest;
import com.cromxt.crom_bucket.dtos.response.MediaResponse;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<MediaResponse> saveFile(MediaUploadRequest mediaUploadRequest);
    Mono<MediaResponse> deleteFile(String fileName);
    Mono<Long> getAvailableSpace();
}
