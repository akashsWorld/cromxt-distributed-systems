package com.cromxt.cloudstore.service;

import com.cromxt.cloudstore.dtos.requests.MediaUploadRequest;
import com.cromxt.cloudstore.dtos.response.FileResponse;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface MediaService {
    Mono<String> saveFile(FilePart file, String fileName, Boolean hlsStatus, Long fileSize);
}
