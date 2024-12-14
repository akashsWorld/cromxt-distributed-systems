package com.cromxt.file_handler.controller;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import com.cromxt.file.handler.dtos.response.ErrorResponse;
import com.cromxt.file_handler.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public Mono<ResponseEntity<Object>> addBucket(@RequestBody BucketRequest
                                                  bucketRequest){
        Mono<String> voidMono = adminService.addBucket(bucketRequest);
        return voidMono.then(
                Mono.just(
                        ResponseEntity.status(HttpStatus.CREATED).build()
                )
        ).onErrorResume(throwable -> Mono.just(
               new ResponseEntity<>(
                       new ErrorResponse("Something went wrong", throwable.getMessage()),
                       HttpStatus.INTERNAL_SERVER_ERROR)
        ));
    }

}
