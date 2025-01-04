package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.service.FileService;
import com.cromxt.files.proto.*;
import com.cromxt.grpc.MediaHeadersKey;
import io.grpc.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
public class MediaHandlerGRPCServiceImpl extends ReactorMediaHandlerServiceGrpc.MediaHandlerServiceImplBase {

    private final FileService fileService;

    //    Handle the upload request in reactive way(Using reactive types Mono and Flux.)
    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request) {

        MediaMetaData metaData = MediaHeadersKey.MEDIA_META_DATA.getContextKey().get(Context.current());

        Path absoluteResourcesPath = Paths.get(fileService.createFilePath(metaData.getContentType()));

        System.out.println(absoluteResourcesPath.getFileName());
        return Mono.create(sink -> {
                            try (FileOutputStream fileOutputStream = new FileOutputStream(absoluteResourcesPath.toFile())) {

                                request.subscribeOn(Schedulers.boundedElastic()).subscribe(fileUploadRequest -> {
                                    try {
                                        fileOutputStream.write(fileUploadRequest.toByteArray());
                                    } catch (Exception e) {
                                        sink.error(e);
                                    }
                                });
                            } catch (Exception e) {
                                sink.error(e);
                            }
                        }
                )
                .onErrorResume(e -> Mono.just(MediaUploadResponse.newBuilder().setStatus(MediaUploadStatus.ERROR).build()))
                .thenReturn(MediaUploadResponse.newBuilder()
                        .setFileId(absoluteResourcesPath.getFileName().toString())
                        .setStatus(MediaUploadStatus.SUCCESS).build());
    }



}
