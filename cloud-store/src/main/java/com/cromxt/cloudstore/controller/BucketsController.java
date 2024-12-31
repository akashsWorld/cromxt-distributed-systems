package com.cromxt.cloudstore.controller;

import com.cromxt.file.handler.dtos.requests.BucketObjects;
import com.cromxt.cloudstore.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/buckets")
@RequiredArgsConstructor
public class BucketsController {

    private final BucketService bucketService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<BucketObjects> getBuckets() {
        return bucketService.fidAllBuckets();
    }
}
