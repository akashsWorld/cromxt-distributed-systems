package com.cromxt.mediaserver.controller;

import com.cromxt.common.crombucket.dtos.mediaserver.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.UpdateMediaRequest;
import com.cromxt.mediaserver.repository.MediaRepository;
import com.cromxt.mediaserver.service.MediaClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/service/media")
@RequiredArgsConstructor
public class MediaServiceController {

    private final MediaClientService mediaClientService;
    private final MediaRepository mediaRepository;

    @PostMapping
    public ResponseEntity<Mono<String>> createMedia(
            @RequestBody NewMediaRequest mediaRequest
            ){
        return ResponseEntity.accepted().body(mediaClientService.createMedia(mediaRequest));
    }

    @PutMapping(value = "/{mediaId}")
    public ResponseEntity<Mono<Void>> updateMedia(
            @PathVariable(name = "mediaId") String mediaId,
            @RequestBody UpdateMediaRequest updateMediaRequest
            ){
        return ResponseEntity.accepted().body(mediaClientService.updateMedia(mediaId,updateMediaRequest));
    }


}
