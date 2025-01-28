package com.cromxt.cloudstore.service;

import com.cromxt.cloudstore.dtos.requests.MediaUploadRequest;
import com.cromxt.cloudstore.dtos.response.FileResponse;
import reactor.core.publisher.Mono;

public interface MediaService {
    Mono<FileResponse> saveFile(MediaUploadRequest fileUploadRequest);
}
