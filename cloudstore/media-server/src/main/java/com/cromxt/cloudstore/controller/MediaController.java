package com.cromxt.cloudstore.controller;

import com.cromxt.cloudstore.dtos.requests.MediaUploadRequest;
import com.cromxt.cloudstore.service.MediaService;

import lombok.RequiredArgsConstructor;

import javax.print.attribute.standard.Media;

import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    @PostMapping
    public Mono<ResponseEntity<Void>> uploadFile(
        @RequestPart("media") FilePart media
    ) {
        
        System.out.println(media.filename());
        return Mono.just(ResponseEntity.ok().build());
    }
}
