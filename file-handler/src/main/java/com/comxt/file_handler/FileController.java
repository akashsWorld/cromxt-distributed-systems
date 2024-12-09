package com.comxt.file_handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    @GetMapping
    public Mono<String> getFile() {
        return Mono.just("Hello World");
    }

}
