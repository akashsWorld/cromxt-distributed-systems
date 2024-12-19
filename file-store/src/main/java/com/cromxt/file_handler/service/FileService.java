package com.cromxt.file_handler.service;

import com.cromxt.file_handler.dtos.requests.FileUploadRequest;
import com.cromxt.file_handler.dtos.response.FileResponse;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<FileResponse> saveFile(FileUploadRequest fileUploadRequest);
}
