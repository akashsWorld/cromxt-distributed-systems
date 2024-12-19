package com.cromxt.bucket_gateway.controller;


import com.cromxt.bucket_gateway.service.RouterService;
import com.cromxt.bucket_gateway.service.impl.DynamicRouteService;
import com.cromxt.file.handler.dtos.requests.BucketRequest;
import com.cromxt.file.handler.dtos.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/gateway")
@RequiredArgsConstructor
@Slf4j
public class GatewayController {


    private final RouterService dynamicRouteService;


    @PostMapping
    public Mono<ResponseEntity<Object>> addServer(@RequestBody BucketRequest bucketRequest) {
        return dynamicRouteService.addRoute(bucketRequest).then(
                Mono.just(
                        ResponseEntity.status(HttpStatus.CREATED).build()
                ))
                .onErrorResume(e->{
                    log.error(e.getMessage(), e);
                    return Mono.just(
                            new ResponseEntity<>(
                                    new ErrorResponse("Something went Wrong", e.getMessage()),
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            )
                    );
                });
    }
    @PutMapping("/{routeId}")
    public Mono<ResponseEntity<Object>> updateRoute(@PathVariable String routeId , @RequestBody BucketRequest bucketRequest) {
        return dynamicRouteService.updateRoute(routeId,bucketRequest).then(
                        Mono.just(
                                ResponseEntity.status(HttpStatus.ACCEPTED).build()
                        ))
                .onErrorResume(e->{
                    log.error(e.getMessage(), e);
                    return Mono.just(
                            new ResponseEntity<>(
                                    new ErrorResponse("Something went Wrong", e.getMessage()),
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            )
                    );
                });
    }
    @DeleteMapping("/{routeId}")
    public Mono<ResponseEntity<Object>> deleteRoute(@PathVariable String routeId,@RequestBody BucketRequest bucketRequest) {
        return dynamicRouteService.deleteRoute(routeId).then(
                        Mono.just(
                                ResponseEntity.status(HttpStatus.OK).build()
                        ))
                .onErrorResume(e->{
                    log.error(e.getMessage(), e);
                    return Mono.just(
                            new ResponseEntity<>(
                                    new ErrorResponse("Something went Wrong", e.getMessage()),
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            )
                    );
                });
    }

}
