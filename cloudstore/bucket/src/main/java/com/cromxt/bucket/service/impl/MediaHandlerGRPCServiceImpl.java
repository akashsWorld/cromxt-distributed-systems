package com.cromxt.bucket.service.impl;

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
                    FileDetails fileDetails = fileService.getFileDetails(mediaMetaData);
                    System.out.println(fileDetails);

                    try (FileOutputStream fileOutputStream = new FileOutputStream(fileDetails.getAbsolutePath())) {
                                request.subscribeOn(Schedulers.boundedElastic()).subscribe(fileUploadRequest -> {
                                    System.out.println("Writing data on the disk.");
                                    try {
                                        fileOutputStream.write(fileUploadRequest.getFile().toByteArray());
                                    } catch (IOException e) {
                                        log.error("Unable to write data on disk.");
                                        sink.error(e);
                                    }
                                });
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
