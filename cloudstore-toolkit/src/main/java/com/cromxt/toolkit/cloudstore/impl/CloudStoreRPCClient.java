package com.cromxt.toolkit.cloudstore.impl;


import com.cromxt.grpc.MediaHeadersKey;
import com.cromxt.proto.files.*;
import com.cromxt.toolkit.cloudstore.BucketDetails;
import com.cromxt.toolkit.cloudstore.CloudStoreClient;
import com.cromxt.toolkit.cloudstore.CromxtUserDetails;
import com.cromxt.toolkit.cloudstore.exceptions.CloudStoreServerException;
import com.cromxt.toolkit.cloudstore.metadata.MediaObjectMetaData;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




public class CloudStoreRPCClient implements CloudStoreClient {


    private String API_KEY;
    private WebClient webClient;

    public CloudStoreRPCClient(CromxtUserDetails cromxtUserDetails) {
        this.API_KEY= cromxtUserDetails.getApiKey();
        this.webClient = cromxtUserDetails.getWebClient();
    }

    @Override
    public String saveFile(FilePart file) {

        return "";
    }

    @Override
    public String saveFile(MultipartFile file) {
        return "";
    }

    private Mono<BucketDetails> getBucketDetails(
            MediaMetaData mediaMetaData
    ){
        return webClient.post()
                .header("Api-Key",API_KEY)
                .bodyValue(mediaMetaData)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> Mono.error(new RuntimeException("Some error occurred.")))
                .bodyToMono(BucketDetails.class)
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
