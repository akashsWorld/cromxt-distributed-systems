package com.cromxt.bucket.controller;


import com.cromxt.bucket.service.MediaObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaObjectService mediaObjectService;

    @GetMapping("/{mediaId}")
    public ResponseEntity<Flux<DataBuffer>> getMedia(@PathVariable String mediaId) {
        return ResponseEntity.ok(Flux.empty());
    }

    @DeleteMapping("/{mediaId}")
    public Mono<Void> deleteMedia(@PathVariable String mediaId) {
        return Mono.empty();
    }

}
