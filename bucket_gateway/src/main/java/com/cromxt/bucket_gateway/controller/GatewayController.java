package com.cromxt.bucket_gateway.controller;


import com.cromxt.bucket_gateway.service.DynamicRouteService;
import com.cromxt.file.handler.dtos.requests.BucketRequest;
import com.cromxt.file.handler.dtos.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/gateway")
@RequiredArgsConstructor
@Slf4j
public class GatewayController {


    private final DynamicRouteService dynamicRouteService;


    @PostMapping
    public Mono<ResponseEntity<Object>> addServer(@RequestBody BucketRequest bucketRequest) {
        return dynamicRouteService.addRoute(bucketRequest).then(
                Mono.just(
                        new ResponseEntity<>(null, HttpStatus.CREATED)
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
