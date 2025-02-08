package com.cromxt.toolkit.cloudstore.impl;


import com.cromxt.grpc.MediaHeadersKey;
import com.cromxt.proto.files.*;
import com.cromxt.routeing.BucketDetails;
import com.cromxt.routeing.MediaDetails;
import com.cromxt.toolkit.cloudstore.CromxtUserDetails;
import com.cromxt.toolkit.cloudstore.ReactiveCloudStoreClient;
import com.cromxt.toolkit.cloudstore.exceptions.CloudStoreServerException;
import com.cromxt.toolkit.cloudstore.metadata.MediaObjectMetaData;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;


public class ReactiveCloudStoreRPCClientImpl implements ReactiveCloudStoreClient {


    private final String API_KEY;
    private final WebClient webClient;

    public ReactiveCloudStoreRPCClientImpl(CromxtUserDetails cromxtUserDetails, WebClient.Builder webclientBuilder) {
        this.API_KEY= cromxtUserDetails.getApiKey();
        this.webClient = webclientBuilder.baseUrl("").build();
    }

    @Override
    public Mono<String> saveFile(Long contentLength, Flux<DataBuffer> data) {


        return Mono.empty();
    }



    private Mono<BucketDetails> getBucketDetails(){
//       TODO: Add all media details;
        MediaDetails mediaDetails = new MediaDetails();

        return webClient
                .post()
                .header("Api-Key",API_KEY)
                .bodyValue(mediaDetails)
                .retrieve()
                .onStatus(HttpStatusCode::isError,(clientResponse -> Mono.error(new CloudStoreServerException("Some error occurred."))))
                .bodyToMono(BucketDetails.class);
    }


    private Mono<String> uploadFile(Flux<DataBuffer> fileData,
                                   MediaObjectMetaData mediaObjectMetadata,
                                   BucketDetails bucketDetails) {

        ManagedChannel channel = createNettyManagedChannel(bucketDetails);

        Metadata headers = generateHeaders(mediaObjectMetadata);
        // Use of reactive implementation instead of blocking.

        ReactorMediaHandlerServiceGrpc.ReactorMediaHandlerServiceStub reactorMediaHandlerServiceStub = ReactorMediaHandlerServiceGrpc
                .newReactorStub(channel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(headers));

        Flux<MediaUploadRequest> data = fileData
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return MediaUploadRequest
                            .newBuilder()
                            .setFile(ByteString.copyFrom(bytes))
                            .build();
                });
//        return data.as(reactorMediaHandlerServiceStub::uploadFile)
//                .flatMap(fileUploadResponse -> {
//                    channel.shutdown();
//                    if (fileUploadResponse.getStatus() == OperationStatus.ERROR) {
//                        return Mono.error(new ClientException(fileUploadResponse.getErrorMessage()));
//                    }
//
//                    return Mono.just(fileUploadResponse.getFileName());
//                });

        return reactorMediaHandlerServiceStub.uploadFile(data).flatMap(fileUploadResponse -> {
                    channel.shutdown();
                    if (fileUploadResponse.getStatus() == OperationStatus.ERROR) {
                        return Mono.error(new CloudStoreServerException(fileUploadResponse.getErrorMessage()));
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


    private Metadata generateHeaders(MediaObjectMetaData mediaObjectMetadata) {

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
