package com.cromxt.bucket.controller;


import com.cromxt.bucket.service.MediaObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/objects")
@RequiredArgsConstructor
public class MediaController {

    private final MediaObjectService mediaObjectService;

    @GetMapping(value = "/{objectId}",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Flux<DataBuffer>> getObject(@PathVariable String objectId) {
        return ResponseEntity.ok(mediaObjectService.getFile(objectId));
    }

    @DeleteMapping("/{mediaId}")
    public Mono<Void> deleteMedia(@PathVariable String mediaId) {
        return Mono.empty();
    }

}
