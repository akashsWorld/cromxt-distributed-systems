package com.cromxt.crom_bucket.controller;

import com.cromxt.crom_bucket.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/server")
@RequiredArgsConstructor
public class ServerController {

    private final FileService fileService;

    @GetMapping("/get-available-space")
    public Mono<ResponseEntity<Map<String,Long>>> dummyRequest(){
        return fileService.getAvailableSpace().map(availableSpace -> new ResponseEntity<>(Map.of("availableSpace",availableSpace), HttpStatus.OK));
    }
}
