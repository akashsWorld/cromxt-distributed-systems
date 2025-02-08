package com.cromxt.routeservice.controller;

import com.cromxt.common.requests.client.requests.MediaMetadata;
import com.cromxt.routeservice.dtos.BucketDetails;
import com.cromxt.routeservice.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/routing")
public record RouterController(
        RouteService routeService) {
    @PostMapping("/get-bucket-id")
    public Mono<ResponseEntity<BucketDetails>> getBucketId(@RequestBody MediaMetadata fileMetaData) {
        System.out.println(fileMetaData);
        return Mono
                .just(new ResponseEntity<>(new BucketDetails("bucket-1", "192.168.0.146", 9091, 9090), HttpStatus.OK));
    }
}
