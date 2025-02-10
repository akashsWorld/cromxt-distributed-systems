package com.cromxt.routeservice.controller;

import com.cromxt.common.routeing.BucketDetails;
import com.cromxt.common.routeing.MediaDetails;
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
    public Mono<ResponseEntity<BucketDetails>> getBucketId(
            @RequestBody MediaDetails mediaDetails) {
        System.out.println(mediaDetails);
        return Mono
                .just(new ResponseEntity<>(new BucketDetails("bucket-1", "192.168.0.181", 9090, 9091), HttpStatus.OK));
    }
}
