package com.cromxt.cloudstore.clients;

import com.cromxt.dtos.response.BucketResponse;
import com.cromxt.files.proto.MediaHandlerServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service(value="bucketGRPCClient")
public class BucketGRPCClient implements BucketClient {

//    TODO:Implement the GRPC client

    @Override
    public Mono<String> uploadFile(Flux<DataBuffer> fileData, String bucketUrl, String fileId) {
        return null;
    }

    private MediaHandlerServiceGrpc.MediaHandlerServiceStub getMediaHandlerServiceStub(BucketResponse bucketResponse) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                        bucketResponse.url(),
                        Integer.parseInt(bucketResponse.port())
                )
                .usePlaintext()
                .build();

        return MediaHandlerServiceGrpc.newStub(channel);
    }

}
