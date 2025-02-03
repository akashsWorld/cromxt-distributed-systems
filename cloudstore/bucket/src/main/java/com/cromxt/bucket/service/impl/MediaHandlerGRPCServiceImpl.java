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

import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
@Slf4j
public class MediaHandlerGRPCServiceImpl extends ReactorMediaHandlerServiceGrpc.MediaHandlerServiceImplBase {

    private final FileService fileService;

    //    Handle the upload request in reactive way(Using reactive types Mono and Flux.)
    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request) {
        return Mono.create(sink -> {
                            MediaMetaData mediaMetaData = MediaHeadersKey.MEDIA_META_DATA.getContextKey().get(Context.current());

                            FileDetails fileDetails = null;
                            try {
                                fileDetails = fileService.getFileDetails(mediaMetaData);
                            } catch (MediaOperationException e) {
                                sink.error(e);
                                return;
                            }
                            FileOutputStream fileOutputStream = null;

                            try {

                                fileOutputStream = new FileOutputStream(fileDetails.getAbsolutePath());
                                FileOutputStream finalFileOutputStream = fileOutputStream; // effectively final variable for use in lambda
                                request.subscribeOn(Schedulers.boundedElastic()).doOnNext(fileUploadRequest -> {

                                    try {
                                        finalFileOutputStream.write(fileUploadRequest.getFile().toByteArray());
                                    } catch (IOException e) {
                                        log.error(e.getMessage());
                                        e.printStackTrace(System.err);
                                        log.error("Unable to write data on disk.");
                                        sink.error(e);
                                    }
                                }).doOnComplete(() -> {
                                    try {
                                        if (finalFileOutputStream != null) {
                                            finalFileOutputStream.close();
                                        }
                                    } catch (IOException e) {
                                        log.error("Unable to close file stream.");
                                        sink.error(e);
                                    }
                                }).subscribe();
                            } catch (IOException e) {
                                log.error("Unable to create file stream.");
                                sink.error(e);
                            }
                            sink.success();
                        }
                )
                .onErrorResume(e -> Mono.just(MediaUploadResponse.newBuilder().setStatus(MediaUploadStatus.ERROR).build()))
                .thenReturn(MediaUploadResponse.newBuilder()
                        .setFileId("some-long-file-id")
                        .setStatus(MediaUploadStatus.SUCCESS).build());
    }


}
