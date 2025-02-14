package com.cromxt.bucketgateway.controller;


import com.cromxt.bucketgateway.service.RouterService;
import com.cromxt.dtos.client.requests.NewBucketRequest;
import com.cromxt.dtos.client.response.ResponseState;
import com.cromxt.dtos.client.response.derived.ErrorResponse;
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
    public Mono<ResponseEntity<Object>> addServer(@RequestBody NewBucketRequest newBucketRequest) {
        return dynamicRouteService.addRoute(newBucketRequest).then(
                Mono.just(
                        ResponseEntity.status(HttpStatus.CREATED).build()
                ))
                .onErrorResume(e->{
                    log.error(e.getMessage(), e);
                    return Mono.just(
                            new ResponseEntity<>(
                                    new ErrorResponse("Something went Wrong", ResponseState.ERROR),
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            )
                    );
                });
    }
    @PutMapping("/{routeId}")
    public Mono<ResponseEntity<Object>> updateRoute(@PathVariable String routeId , @RequestBody NewBucketRequest newBucketRequest) {
        return dynamicRouteService.updateRoute(routeId, newBucketRequest).then(
                        Mono.just(
                                ResponseEntity.status(HttpStatus.ACCEPTED).build()
                        ))
                .onErrorResume(e->{
                    log.error(e.getMessage(), e);
                    return Mono.just(
                            new ResponseEntity<>(
                                    new ErrorResponse("Something went Wrong", ResponseState.ERROR),
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            )
                    );
                });
    }
    @DeleteMapping("/{routeId}")
    public Mono<ResponseEntity<Object>> deleteRoute(@PathVariable String routeId,@RequestBody NewBucketRequest newBucketRequest) {
        return dynamicRouteService.deleteRoute(routeId).then(
                        Mono.just(
                                ResponseEntity.status(HttpStatus.OK).build()
                        ))
                .onErrorResume(e->{
                    log.error(e.getMessage(), e);
                    return Mono.just(
                            new ResponseEntity<>(
                                    new ErrorResponse("Something went Wrong", ResponseState.ERROR),
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            )
                    );
                });
    }

}
