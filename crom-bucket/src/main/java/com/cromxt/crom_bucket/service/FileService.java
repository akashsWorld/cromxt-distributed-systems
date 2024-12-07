package com.cromxt.crom_bucket.service;

import com.cromxt.crom_bucket.dtos.request.MediaRequestDTO;
import com.cromxt.crom_bucket.dtos.response.MediaRegisterResponse;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<MediaRegisterResponse> saveFile(MediaRequestDTO mediaRequestDTO);
}
