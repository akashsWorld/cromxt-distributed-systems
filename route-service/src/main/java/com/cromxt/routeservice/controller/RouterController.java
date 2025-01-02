package com.cromxt.routeservice.controller;


import com.cromxt.dtos.requests.FileMetaData;
import com.cromxt.dtos.response.BucketDetails;
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
    public Mono<ResponseEntity<BucketDetails>> getBucketId(@RequestBody FileMetaData fileMetaData){
        System.out.println(fileMetaData);
        return Mono.just(new ResponseEntity<>(new BucketDetails("some_long_bucket_id",8888), HttpStatus.OK));
    }
}
