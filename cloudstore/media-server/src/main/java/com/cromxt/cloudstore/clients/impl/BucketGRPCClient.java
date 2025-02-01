package com.cromxt.cloudstore.clients.impl;

import io.grpc.*;
import io.grpc.netty.NettyChannelBuilder;
import org.springframework.core.io.buffer.DataBuffer;

import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.cloudstore.dtos.MediaObjectMetadata;
import com.cromxt.cloudstore.dtos.response.MediaObjectDetails;
import com.cromxt.dtos.client.response.BucketAddress;
import com.cromxt.grpc.MediaHeadersKey;
import com.cromxt.proto.files.MediaHandlerServiceGrpc;
import com.cromxt.proto.files.MediaMetaData;
import com.cromxt.proto.files.MediaUploadRequest;
import com.cromxt.proto.files.ReactorMediaHandlerServiceGrpc;
import com.google.protobuf.ByteString;

import io.grpc.stub.MetadataUtils;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class BucketGRPCClient implements BucketClient {

        @Override
        public Mono<MediaObjectDetails> uploadFile(Flux<DataBuffer> fileData,
                        MediaObjectMetadata mediaObjectMetadata,
                        BucketAddress bucketAddress) {

                Channel channel = createManagedChannel(bucketAddress);

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
                                        System.out.println(fileUploadResponse.getStatus());
                                        return Mono.empty();
                                });
        }

        private Channel createManagedChannel(BucketAddress bucketAddress) {
            return NettyChannelBuilder
                    .forAddress(bucketAddress.url(), bucketAddress.port())
                    .usePlaintext()
                    .flowControlWindow(NettyChannelBuilder.DEFAULT_FLOW_CONTROL_WINDOW)
                    .build();
        }

        private Metadata generateHeaders(MediaObjectMetadata mediaObjectMetadata) {

                MediaMetaData mediaMetaData = MediaMetaData.newBuilder()
                                .setContentType(mediaObjectMetadata.getContentType())
                                .setHlsStatus(mediaObjectMetadata.getHlsStatus())
                                .build();

                Metadata metadata = new Metadata();

                Metadata.Key<byte[]> metaDataKey = (Metadata.Key<byte[]>) MediaHeadersKey.MEDIA_META_DATA
                                .getMetaDataKey();
                metadata.put(metaDataKey, mediaMetaData.toByteArray());
                return metadata;
        }

        private MediaHandlerServiceGrpc.MediaHandlerServiceStub getMediaHandlerServiceStub(
                        BucketAddress bucketAddress) {

                // To use reactive types this Stub is not used. bu it can also used.
                ManagedChannel channel = ManagedChannelBuilder.forAddress(
                                bucketAddress.url(),
                                bucketAddress.port())
                                .usePlaintext()
                                .build();
                return MediaHandlerServiceGrpc.newStub(channel);
        }

}
