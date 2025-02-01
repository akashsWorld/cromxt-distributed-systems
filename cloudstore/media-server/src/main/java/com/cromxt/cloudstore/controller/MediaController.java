package com.cromxt.cloudstore.controller;

import com.cromxt.cloudstore.dtos.requests.MediaUploadRequest;
import com.cromxt.cloudstore.dtos.response.FileResponse;
import com.cromxt.cloudstore.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping
   public Mono<ResponseEntity<FileResponse>> uploadFile(
           @ModelAttribute MediaUploadRequest mediaUploadRequest
    ) {
        return mediaService.saveFile(mediaUploadRequest).map(ResponseEntity::ok);
    }
}
