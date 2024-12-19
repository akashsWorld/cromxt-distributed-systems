package com.cromxt.route_service.controller;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import com.cromxt.route_service.service.RouteService;
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
    public Flux<BucketRequest> getAllRoutes(){
        return Flux.fromIterable(List.of(
                new BucketRequest("1", "localhost", 8080),
                new BucketRequest("2", "localhost", 8080),
                new BucketRequest("3", "localhost", 8080),
                new BucketRequest("4", "localhost", 8080)
        ));
    }

    @PostMapping
    public Mono<Void> saveRoute(BucketRequest bucketRequest){
        return null;
    }

    @PutMapping("/{routeId}")
    public Mono<Void> updateRoute(@PathVariable String routeId, BucketRequest bucketRequest){
        return null;
    }
    @DeleteMapping("/{routeId}")
    public Mono<Void> deleteRoute(@PathVariable String routeId){
        return null;
    }
}
