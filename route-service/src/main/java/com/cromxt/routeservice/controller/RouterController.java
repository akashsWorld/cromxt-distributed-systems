package com.cromxt.routeservice.controller;


import com.cromxt.file.handler.dtos.requests.BucketObjects;
import com.cromxt.routeservice.service.RouteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
public record RouterController(
        RouteService routeService
) {

    @GetMapping
    public Flux<BucketObjects> getAllRoutes(){
        return Flux.fromIterable(List.of(
                new BucketObjects("1", "localhost", 8080),
                new BucketObjects("2", "localhost", 8080),
                new BucketObjects("3", "localhost", 8080),
                new BucketObjects("4", "localhost", 8080)
        ));
    }

    @GetMapping("/fileSize/{fileSize}")
    public Mono<String> getBucketId(@PathVariable Long fileSize){
        return null;
    }
}
