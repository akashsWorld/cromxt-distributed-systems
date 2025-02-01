package com.cromxt.bucketserver.controller;


import com.cromxt.bucketserver.service.BucketService;
import com.cromxt.dtos.client.requests.NewBucketRequest;
import com.cromxt.dtos.client.response.BucketResponse;
import com.cromxt.dtos.client.response.BucketResponseDTO;
import com.cromxt.dtos.client.response.ResponseState;
import com.cromxt.dtos.client.response.derived.ErrorResponse;
import com.cromxt.dtos.client.response.derived.NewBucketListResponse;
import com.cromxt.dtos.client.response.derived.NewBucketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {


    private final BucketService bucketService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<BucketResponse>> getAllBuckets() {

        Flux<BucketResponseDTO> allBuckets = bucketService.findAllBuckets();

        return allBuckets
                .collectList()
        .map(
                buckets -> new ResponseEntity<BucketResponse>(
                        new NewBucketListResponse(ResponseState.SUCCESS, buckets), HttpStatus.OK)

        ).onErrorResume(e ->
                Mono.just(new ResponseEntity<>(new
                        ErrorResponse(e.getMessage(), ResponseState.ERROR),
                        HttpStatus.BAD_REQUEST)));
    }

    @PostMapping(produces = "application/json")
    public Mono<ResponseEntity<BucketResponse>> addBucket(@RequestBody NewBucketRequest newBucketRequest) {
        return bucketService
                .createBucket(newBucketRequest)
                .map(bucket -> new ResponseEntity<BucketResponse>(new NewBucketResponse(
                        ResponseState.SUCCESS,
                        bucket
                ), HttpStatus.CREATED))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new ErrorResponse(e.getMessage(), ResponseState.ERROR), HttpStatus.BAD_REQUEST)));
    }

    @DeleteMapping("/{bucketId}")
    public Mono<ResponseEntity<BucketResponse>> deleteBucket(@PathVariable String bucketId) {
        return bucketService.deleteBucketById(bucketId)
                .then(Mono.just(new ResponseEntity<>(new BucketResponse(ResponseState.SUCCESS),HttpStatus.ACCEPTED)))
                .onErrorResume(e->Mono.just(new ResponseEntity<>(new ErrorResponse(
                        e.getMessage(),
                        ResponseState.ERROR
                ),HttpStatus.BAD_REQUEST)));
    }

    @PutMapping("/{bucketId}")
    public Mono<ResponseEntity<BucketResponse>> updateBucket(@PathVariable String bucketId,
                                                             @RequestBody NewBucketRequest newBucketRequest) {
        return bucketService.updateBucket(bucketId,newBucketRequest)
                .then(Mono.just(new ResponseEntity<>(new BucketResponse(ResponseState.SUCCESS),HttpStatus.ACCEPTED)))
                .onErrorResume(e->Mono.just(new ResponseEntity<>(new ErrorResponse(
                        e.getMessage(),
                        ResponseState.ERROR
                ),HttpStatus.BAD_REQUEST)));
    }

    @DeleteMapping
    public Mono<ResponseEntity<BucketResponse>> deleteAllBuckets() {
        return bucketService.deleteAllBuckets()
                .then(Mono.just(new ResponseEntity<>(new BucketResponse(ResponseState.SUCCESS),HttpStatus.ACCEPTED)))
                .onErrorResume(e->Mono.just(new ResponseEntity<>(new ErrorResponse(
                        e.getMessage(),
                        ResponseState.ERROR
                ),HttpStatus.BAD_REQUEST)));
    }

}
