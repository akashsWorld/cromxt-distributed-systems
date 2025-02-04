package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.service.FileService;
import com.cromxt.grpc.MediaHeadersKey;
import com.cromxt.proto.files.*;
import io.grpc.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;


@Service
@RequiredArgsConstructor
@Slf4j
public class MediaHandlerGRPCServiceImpl extends ReactorMediaHandlerServiceGrpc.MediaHandlerServiceImplBase {

    private final FileService fileService;

    //    Handle the upload request in reactive way(Using reactive types Mono and Flux.)
    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request) {
        MediaMetaData mediaMetaData = MediaHeadersKey.MEDIA_META_DATA.getContextKey().get(Context.current());

        FileDetails fileDetails = fileService.getFileDetails(mediaMetaData);

        return Mono.create(sink -> {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileDetails.getAbsolutePath());
                request.subscribeOn(Schedulers.boundedElastic())
                        .doOnNext(chunkData -> {
                            try {
                                fileOutputStream.write(chunkData.getFile().toByteArray());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .doOnComplete(() -> {
                            try {
                                fileOutputStream.close();
                                sink.success(MediaUploadResponse.newBuilder()
                                        .setStatus(MediaUploadStatus.SUCCESS)
                                        .setFileId("long-file-id")
                                        .build());
                            } catch (IOException e) {
                                sink.error(e);
                            }
                        })
                        .doOnError(sink::error)
                        .subscribe();
            } catch (IOException e) {
                sink.error(e);
            }
        });
    }


}
