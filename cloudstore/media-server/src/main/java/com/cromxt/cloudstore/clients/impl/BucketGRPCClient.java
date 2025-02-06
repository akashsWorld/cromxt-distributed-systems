package com.cromxt.cloudstore.clients.impl;

import com.cromxt.cloudstore.exception.ClientException;
import com.cromxt.proto.files.*;
import io.grpc.*;
import io.grpc.netty.NettyChannelBuilder;
import org.springframework.core.io.buffer.DataBuffer;

import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.cloudstore.dtos.MediaObjectMetadata;
import com.cromxt.cloudstore.dtos.response.MediaObjectDetails;
import com.cromxt.dtos.client.response.BucketDetails;
import com.cromxt.grpc.MediaHeadersKey;
import com.google.protobuf.ByteString;

import io.grpc.stub.MetadataUtils;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class BucketGRPCClient implements BucketClient {

    @Override
    public Mono<String> uploadFile(Flux<DataBuffer> fileData,
                                   MediaObjectMetadata mediaObjectMetadata,
                                   BucketDetails bucketDetails) {

        ManagedChannel channel = createNettyManagedChannel(bucketDetails);

        Metadata headers = generateHeaders(mediaObjectMetadata);
        // Use of reactive implementation instead of blocking.

        ReactorMediaHandlerServiceGrpc.ReactorMediaHandlerServiceStub reactorMediaHandlerServiceStub = ReactorMediaHandlerServiceGrpc
                .newReactorStub(channel);

        Flux<MediaUploadRequest> data = fileData
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return MediaUploadRequest
                            .newBuilder()
                            .setFile(ByteString.copyFrom(bytes))
                            .build();
                });
        return data.as(reactorMediaHandlerServiceStub.withInterceptors(
                        MetadataUtils.newAttachHeadersInterceptor(headers))::uploadFile)
                .flatMap(fileUploadResponse -> {
                    if (fileUploadResponse.getStatus() == OperationStatus.ERROR) {
                        return Mono.error(new ClientException(fileUploadResponse.getErrorMessage()));
                    }
                    return Mono.just(fileUploadResponse.getFileName());
                });

    }

    private ManagedChannel createNettyManagedChannel(BucketDetails bucketDetails) {
        return NettyChannelBuilder
                .forAddress(bucketDetails.hostName(), bucketDetails.rpcPort())
                .usePlaintext()
                .flowControlWindow(NettyChannelBuilder.DEFAULT_FLOW_CONTROL_WINDOW)
                .build();
    }

    private ManagedChannel createManagedChannel(BucketDetails bucketDetails) {
        return ManagedChannelBuilder.forAddress(bucketDetails.hostName(), bucketDetails.rpcPort())
                .usePlaintext()
                .build();
    }


    private Metadata generateHeaders(MediaObjectMetadata mediaObjectMetadata) {

        MediaMetaData mediaMetaData = MediaMetaData.newBuilder()
                .setContentType(mediaObjectMetadata.getContentType())
                .setContentLength(mediaObjectMetadata.getContentLength())
                .setHlsStatus(mediaObjectMetadata.getHlsStatus())
                .build();

        Metadata metadata = new Metadata();

        Metadata.Key<byte[]> metaDataKey = (Metadata.Key<byte[]>) MediaHeadersKey.MEDIA_META_DATA
                .getMetaDataKey();
        metadata.put(metaDataKey, mediaMetaData.toByteArray());
        return metadata;
    }

    private MediaHandlerServiceGrpc.MediaHandlerServiceStub getMediaHandlerServiceStub(
            BucketDetails bucketDetails) {

        // To use reactive types this Stub is not used. but it can also be used as a blocking stub.
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                        bucketDetails.hostName(),
                        bucketDetails.rpcPort())
                .usePlaintext()
                .build();
        return MediaHandlerServiceGrpc.newStub(channel);
    }

}
