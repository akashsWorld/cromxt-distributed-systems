package com.cromxt.crom_bucket.service;

import com.cromxt.crom_bucket.dtos.request.MediaRequestDTO;
import com.cromxt.crom_bucket.dtos.response.MediaResponse;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<MediaResponse> saveFile(MediaRequestDTO mediaRequestDTO);
    Mono<MediaResponse> deleteFile(MediaRequestDTO mediaRequestDTO);


}
