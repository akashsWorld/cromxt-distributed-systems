package com.cromxt.cloudstore.service.impl;


import com.cromxt.cloudstore.repository.MediaRepository;
import com.cromxt.proto.files.EmptyResponse;
import com.cromxt.proto.files.MediaObjectDetails;
import com.cromxt.proto.files.OperationStatus;
import com.cromxt.proto.files.ReactorMediaObjectServiceGrpc;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class MediaObjectRPCService extends ReactorMediaObjectServiceGrpc.MediaObjectServiceImplBase {

    private final MediaRepository mediaRepository;

    @Override
    public Mono<EmptyResponse> deleteMediaObject(Mono<MediaObjectDetails> request) {
        return request.flatMap(mediaObjectDetails -> mediaRepository.findByName(mediaObjectDetails.getFileName()).flatMap(mediaObjects -> mediaRepository.delete(mediaObjects).then(
                Mono.just(EmptyResponse.newBuilder()
                        .setStatus(OperationStatus.SUCCESS)
                        .build())
        )));
    }
}
