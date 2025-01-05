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
                new NewBucketRequest("1", "localhost", 8080),
                new NewBucketRequest("2", "localhost", 8080),
                new NewBucketRequest("3", "localhost", 8080),
                new NewBucketRequest("4", "localhost", 8080)
        ));
    }

    @PostMapping
    public Mono<Void> saveRoute(NewBucketRequest newBucketRequest){
        return null;
    }

    @PutMapping("/{routeId}")
    public Mono<Void> updateRoute(@PathVariable String routeId, NewBucketRequest newBucketRequest){
        return null;
    }
    @DeleteMapping("/{routeId}")
    public Mono<Void> deleteRoute(@PathVariable String routeId){
        return null;
    }
}
