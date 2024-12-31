package com.cromxt.routeservice.controller;

import com.cromxt.dtos.requests.BucketObjects;
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
    public Flux<BucketObjects> getAllRoutes(){
        return Flux.fromIterable(List.of(
                new BucketObjects("1", "localhost", 8080),
                new BucketObjects("2", "localhost", 8080),
                new BucketObjects("3", "localhost", 8080),
                new BucketObjects("4", "localhost", 8080)
        ));
    }

    @PostMapping
    public Mono<Void> saveRoute(BucketObjects bucketObjects){
        return null;
    }

    @PutMapping("/{routeId}")
    public Mono<Void> updateRoute(@PathVariable String routeId, BucketObjects bucketObjects){
        return null;
    }
    @DeleteMapping("/{routeId}")
    public Mono<Void> deleteRoute(@PathVariable String routeId){
        return null;
    }
}
