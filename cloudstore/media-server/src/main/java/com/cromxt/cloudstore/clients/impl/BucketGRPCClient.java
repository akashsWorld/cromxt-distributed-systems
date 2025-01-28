package com.cromxt.cloudstore.clients.impl;

import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.cloudstore.dtos.MediaObjectMetadata;
import com.cromxt.cloudstore.dtos.response.MediaObjectDetails;
import com.cromxt.dtos.client.response.BucketAddress;
import com.cromxt.proto.files.MediaHandlerServiceGrpc;
import com.cromxt.proto.files.MediaUploadRequest;
import com.cromxt.proto.files.ReactorMediaHandlerServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
public class BucketGRPCClient implements BucketClient {

    @Override
    public Mono<MediaObjectDetails> uploadFile(Flux<DataBuffer> fileData,
                                               MediaObjectMetadata mediaObjectDetails,
                                               BucketAddress bucketAddress) {
        ReactorMediaHandlerServiceGrpc.ReactorMediaHandlerServiceStub reactorMediaHandlerServiceStub =
                getReactorMediaHandlerServiceStub(
                        bucketAddress,
                        generateHeaders(bucketAddress)
                );

        Flux<MediaUploadRequest> data = fileData
                .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            return MediaUploadRequest
                                    .newBuilder()
                                    .setFile(ByteString.copyFrom(bytes))
                                    .build();
                        }
                );
        return data.as(reactorMediaHandlerServiceStub.withInterceptors(
//                TODO:Add the interceptor for the grpc call.
                )::uploadFile)
                .flatMap(
                        fileUploadResponse ->
//                                TODO: Handle the response.
                                Mono.empty()
                );
    }

    private MediaHandlerServiceGrpc.MediaHandlerServiceStub getMediaHandlerServiceStub(BucketAddress bucketAddress) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                        bucketAddress.url(),
                        bucketAddress.port()
                )
                .usePlaintext()
                .build();
        return MediaHandlerServiceGrpc.newStub(channel);
    }


    private Metadata generateHeaders(BucketAddress bucketAddress) {
        return null;
    }
//    Use of reactive implementation instead of blocking.
    private ReactorMediaHandlerServiceGrpc.ReactorMediaHandlerServiceStub getReactorMediaHandlerServiceStub(
            BucketAddress bucketAddress,
            Metadata headers
    ) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(
                        bucketAddress.url(),
                        bucketAddress.port()
                )
                .intercept(MetadataUtils.newAttachHeadersInterceptor(headers))
                .usePlaintext()
                .build();

//        Channel interceptedChannel = ClientInterceptors.intercept(managedChannel, )
        return ReactorMediaHandlerServiceGrpc.newReactorStub(managedChannel);
    }
}
