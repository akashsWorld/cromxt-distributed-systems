package com.cromxt.cloudstore.service;

import com.cromxt.cloudstore.dtos.requests.FileUploadRequest;
import com.cromxt.cloudstore.dtos.response.FileResponse;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<FileResponse> saveFile(FileUploadRequest fileUploadRequest);
}
