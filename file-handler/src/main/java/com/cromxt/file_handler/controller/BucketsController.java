package com.cromxt.file_handler.controller;

import com.cromxt.file.handler.dtos.BucketsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buckets")
public class BucketsController {

    @GetMapping
    public Flux<BucketsResponse> getBuckets() {
        return Flux.fromIterable(List.of(
                new BucketsResponse("1", "localhost", "8090"),
                new BucketsResponse("2", "localhost", "8090")
        ));
    }
}
