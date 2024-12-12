package com.comxt.file_handler.controller;


import com.comxt.file_handler.dtos.request.AddBucketRequest;
import com.comxt.file_handler.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BucketService bucketService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public String geAllAvailableServer(){
        return "Hello Admin";
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<ResponseEntity<Object>> addServer(@RequestBody AddBucketRequest addBucketRequest){
        return bucketService.addBucket(addBucketRequest)
                .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

}
