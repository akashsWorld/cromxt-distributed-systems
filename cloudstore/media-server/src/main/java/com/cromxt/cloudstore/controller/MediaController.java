package com.cromxt.cloudstore.controller;

import com.cromxt.cloudstore.dtos.requests.MediaUploadRequest;
import com.cromxt.cloudstore.dtos.response.FileResponse;
import com.cromxt.cloudstore.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping
   public Mono<ResponseEntity<FileResponse>> uploadFile(
           @ModelAttribute MediaUploadRequest mediaUploadRequest,
           @RequestParam (name= "hlsStatus", required = false, defaultValue = "false") boolean hlsStatus,
           @RequestParam(name = "fileName",required = false, defaultValue = "cloud-store-file") String fileName
    ) {
        return mediaService.saveFile(mediaUploadRequest, fileName, hlsStatus).map(ResponseEntity::ok);
    }
}
