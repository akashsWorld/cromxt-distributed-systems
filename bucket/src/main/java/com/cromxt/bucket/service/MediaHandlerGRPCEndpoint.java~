package com.cromxt.bucket.service;

import com.cromxt.files.proto.FileUploadRequest;
import com.cromxt.files.proto.FileUploadResponse;
import com.cromxt.files.proto.ReactorMediaHandlerServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@GrpcService
public class MediaHandlerGRPCEndpoint extends ReactorMediaHandlerServiceGrpc.MediaHandlerServiceImplBase {

//    Handle the upload request in reactive way(Using reactive types Mono and Flux.)
    @Override
    public Mono<FileUploadResponse> uploadFile(Flux<FileUploadRequest> request) {
        return Mono.empty();
    }
}
