package com.cromxt.system.controller;


import com.cromxt.common.dtos.BaseResponse;
import com.cromxt.common.dtos.CromxtResponseStatus;
import com.cromxt.common.dtos.ErrorResponse;
import com.cromxt.system.dtos.NewBucketRequest;
import com.cromxt.system.dtos.SavedBucketRespnse;
import com.cromxt.system.dtos.response.BucketListResponse;
import com.cromxt.system.dtos.response.BucketResponse;
import com.cromxt.system.service.BucketService;
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
    public Mono<ResponseEntity<BaseResponse>> getAllBuckets() {
        Mono<BucketListResponse> allBuckets = bucketService.findAllBuckets();
        return allBuckets
                .map(bucketListResponse -> new ResponseEntity<BaseResponse>(bucketListResponse, HttpStatus.OK))
                .onErrorResume(e-> Mono.just(
                        new ResponseEntity<>(
                                new ErrorResponse(e.getMessage(), CromxtResponseStatus.ERROR),
                                HttpStatus.BAD_REQUEST
                        )
                ));

    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Mono<? extends BaseResponse>> addBucket(@RequestBody NewBucketRequest newBucketRequest) {
        Mono<BucketResponse> bucketResponse = bucketService.createBucket(newBucketRequest);
        return bucketResponse.flatMap(ignored -> new ResponseEntity<>(bucketResponse, HttpStatus.CREATED));
    }

    @DeleteMapping("/{bucketId}")
    public Mono<ResponseEntity<SavedBucketRespnse>> deleteBucket(@PathVariable String bucketId) {
        return bucketService.deleteBucketById(bucketId)
                .then(Mono.just(new ResponseEntity<>(new SavedBucketRespnse(ResponseState.SUCCESS), HttpStatus.ACCEPTED)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new ErrorResponse(
                        e.getMessage(),
                        ResponseState.ERROR
                ), HttpStatus.BAD_REQUEST)));
    }

    @PutMapping("/{bucketId}")
    public Mono<ResponseEntity<SavedBucketRespnse>> updateBucket(@PathVariable String bucketId,
                                                                 @RequestBody NewBucketRequest newBucketRequest) {
        return bucketService.updateBucket(bucketId, newBucketRequest)
                .then(Mono.just(new ResponseEntity<>(new SavedBucketRespnse(ResponseState.SUCCESS), HttpStatus.ACCEPTED)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new ErrorResponse(
                        e.getMessage(),
                        ResponseState.ERROR
                ), HttpStatus.BAD_REQUEST)));
    }

    @DeleteMapping
    public Mono<ResponseEntity<SavedBucketRespnse>> deleteAllBuckets() {
        return bucketService.deleteAllBuckets()
                .then(Mono.just(new ResponseEntity<>(new SavedBucketRespnse(ResponseState.SUCCESS), HttpStatus.ACCEPTED)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new ErrorResponse(
                        e.getMessage(),
                        ResponseState.ERROR
                ), HttpStatus.BAD_REQUEST)));
    }

}
