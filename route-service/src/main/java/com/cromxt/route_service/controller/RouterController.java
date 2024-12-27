package com.cromxt.route_service.controller;


import com.cromxt.file.handler.dtos.requests.BucketRequest;
import com.cromxt.route_service.service.RouteService;
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
    public Flux<BucketRequest> getAllRoutes(){
        return Flux.fromIterable(List.of(
                new BucketRequest("1", "localhost", 8080),
                new BucketRequest("2", "localhost", 8080),
                new BucketRequest("3", "localhost", 8080),
                new BucketRequest("4", "localhost", 8080)
        ));
    }

    @GetMapping("/fileSize/{fileSize}")
    public Mono<String> getBucketId(@PathVariable Long fileSize){
        return null;
    }
}
