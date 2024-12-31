package com.cromxt.routeservice.controller;


import com.cromxt.dtos.requests.BucketObjects;
import com.cromxt.dtos.requests.FileMetaData;
import com.cromxt.dtos.response.BucketResponse;
import com.cromxt.routeservice.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routing")
public record RouterController(
        RouteService routeService
) {
    @PostMapping("/get-bucket-id")
    public Mono<ResponseEntity<BucketResponse>> getBucketId(@RequestBody FileMetaData fileMetaData){
        System.out.println(fileMetaData);
        return Mono.just(new ResponseEntity<>(new BucketResponse("some_long_bucket_id"), HttpStatus.OK));
    }
}
