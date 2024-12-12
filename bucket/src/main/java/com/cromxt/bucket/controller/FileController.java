package com.cromxt.bucket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/files")
public class FileController {

    @PostMapping
    public Mono<ResponseEntity<Void>> uploadFile() {
        System.out.println("File uploaded!");
        return Mono.empty();
    }

}
