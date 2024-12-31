package com.cromxt.cloudstore.controller;

import com.cromxt.cloudstore.dtos.requests.FileUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/files")
@RequiredArgsConstructor
public class FileHandler {

    @PostMapping(value = "/upload")
    public Mono<ResponseEntity<Void>> uploadFile(
            @ModelAttribute FileUploadRequest fileUploadRequest
    ) {
        return Mono.just(ResponseEntity.ok().build());
    }
}
