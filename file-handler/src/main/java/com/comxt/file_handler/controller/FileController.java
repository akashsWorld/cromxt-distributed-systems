package com.comxt.file_handler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileService fileService;

    @PostMapping
    public Mono<String> getFile() {
        return Mono.just("Hello World");
    }

}
