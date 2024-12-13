package com.cromxt.bucket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/medias")
public class FileController {

    @PostMapping(value = "/upload")
    public Mono<ResponseEntity<String>> uploadFile(@RequestPart(value = "file") FilePart file) {
        System.out.println("File uploaded!");
        System.out.println(file.filename());
        return Mono.just(new ResponseEntity<>(file.filename(), HttpStatus.CREATED));
    }
    @GetMapping(value = "/file/{fileName}")
    public Mono<ResponseEntity<String>> getFile(@PathVariable String fileName) {
        System.out.println("Found file: " + fileName);
        return Mono.just(new ResponseEntity<>(fileName, HttpStatus.OK));
    }

}
