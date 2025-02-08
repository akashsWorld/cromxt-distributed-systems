package com.cromxt.mediaserver.controller;

import com.cromxt.mediaserver.service.MediaClientService;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/media")
public record MediaController(
        MediaClientService mediaClientService
) {


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
