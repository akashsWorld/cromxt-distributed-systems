package com.cromxt.routeservice.controller;

import com.cromxt.dtos.client.requests.NewBucketRequest;
import com.cromxt.routeservice.service.RouteService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public record AdminController (
        RouteService routeService
){

    @GetMapping
    public Flux<NewBucketRequest> getAllRoutes(){
        return Flux.fromIterable(List.of(

        ));
    }

    @PostMapping
    public Mono<Void> saveBucket(NewBucketRequest newBucketRequest){
        return null;
    }

    @PutMapping("/{bucketId}")
    public Mono<Void> updateBucket(@PathVariable String bucketId, NewBucketRequest newBucketRequest){
        return null;
    }
    @DeleteMapping("/{bucketId}")
    public Mono<Void> deleteBucket(@PathVariable String bucketId){
        return null;
    }
}
