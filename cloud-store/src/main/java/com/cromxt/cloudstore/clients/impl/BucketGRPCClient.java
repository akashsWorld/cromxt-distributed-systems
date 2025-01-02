package com.cromxt.cloudstore.clients.impl;

import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.dtos.response.BucketDetails;
import com.cromxt.files.proto.FileUploadRequest;
import com.cromxt.files.proto.FileUploadResponse;
import com.cromxt.files.proto.MediaHandlerServiceGrpc;
import com.cromxt.files.proto.ReactorMediaHandlerServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
public class BucketGRPCClient implements BucketClient {

    @Override
    public Mono<String> uploadFile(Flux<DataBuffer> fileData, BucketDetails bucketDetails) {
        ReactorMediaHandlerServiceGrpc.ReactorMediaHandlerServiceStub reactorMediaHandlerServiceStub =
                getReactorMediaHandlerServiceStub(bucketDetails);

        Flux<FileUploadRequest> data = fileData
                .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            return FileUploadRequest.newBuilder().setFile(ByteString.copyFrom(bytes)).build();
                        }
                );
        return reactorMediaHandlerServiceStub
                .uploadFile(data)
                .map(FileUploadResponse::getFileId);
    }

    private MediaHandlerServiceGrpc.MediaHandlerServiceStub getMediaHandlerServiceStub(BucketDetails bucketDetails) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                        bucketDetails.url(),
                        bucketDetails.port()
                )
                .usePlaintext()
                .build();
        return MediaHandlerServiceGrpc.newStub(channel);
    }

//    Use of reactive implementation instead of blocking.
    public ReactorMediaHandlerServiceGrpc.ReactorMediaHandlerServiceStub getReactorMediaHandlerServiceStub(BucketDetails bucketDetails) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                        bucketDetails.url(),
                        bucketDetails.port()
                )
                .usePlaintext()
                .build();
        return ReactorMediaHandlerServiceGrpc.newReactorStub(channel);
    }
}
