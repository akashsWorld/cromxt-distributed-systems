package com.cromxt.mediaserver.service;


import com.cromxt.common.dtos.mediaserver.requests.NewMediaRequest;
import com.cromxt.common.dtos.mediaserver.requests.UpdateMediaRequest;
import reactor.core.publisher.Mono;

public interface MediaClientService {

    Mono<String> createMedia(NewMediaRequest newMediaRequest);

    Mono<Void> updateMedia(String mediaId, UpdateMediaRequest mediaRequest);
}
