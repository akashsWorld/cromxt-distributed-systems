package com.cromxt.cloudstore.controller;

import com.cromxt.cloudstore.dtos.requests.MediaUploadRequest;
import com.cromxt.cloudstore.dtos.response.FileResponse;
import com.cromxt.cloudstore.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(value = "/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<FileResponse>> uploadFile(
            @RequestPart(name = "media") FilePart file,
            @RequestParam(name = "hlsStatus", required = false, defaultValue = "false") boolean hlsStatus,
            @RequestParam(name = "fileName", required = false, defaultValue = "cloud-store-file") String fileName,
            @RequestHeader(name = "Content-Length") Long fileSize
    ) {
//        System.out.println(fileSize);


        System.out.println("Hello");
        return mediaService.saveFile(file, fileName, hlsStatus, fileSize).map(ResponseEntity::ok);
//        return Mono.empty();
    }

    private Long getFileSize(FilePart mediaObject) {
        return mediaObject
                .content()
                .map(dataBuffer -> {
                    long size = dataBuffer.readableByteCount();
                    dataBuffer.readPosition(0);
                    return size;
                })
                .reduce(Long::sum).block();
    }

}
