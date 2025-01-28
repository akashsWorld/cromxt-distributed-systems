package com.cromxt.routeservice.controller;


import com.cromxt.dtos.client.requests.MediaMetadata;
import com.cromxt.dtos.client.response.BucketAddress;
import com.cromxt.routeservice.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/routing")
public record RouterController(
        RouteService routeService
) {
    @PostMapping("/get-bucket-id")
    public Mono<ResponseEntity<BucketAddress>> getBucketId(@RequestBody MediaMetadata fileMetaData){
        System.out.println(fileMetaData);
        return Mono.just(new ResponseEntity<>(new BucketAddress("localhost",8888), HttpStatus.OK));
    }
}
