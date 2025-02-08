package com.cromxt.mediaserver.service;


import com.cromxt.mediaserver.dtos.NewMediaRequest;
import com.cromxt.mediaserver.dtos.UpdateMediaRequest;
import reactor.core.publisher.Mono;

public interface MediaClientService {

    Mono<String> createMedia(NewMediaRequest newMediaRequest);

    Mono<Void> updateMedia(String mediaId,UpdateMediaRequest mediaRequest);
}
