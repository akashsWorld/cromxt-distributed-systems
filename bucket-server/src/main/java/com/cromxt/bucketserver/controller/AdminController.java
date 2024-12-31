package com.cromxt.bucketserver.controller;


import com.cromxt.bucketserver.service.BucketService;
import com.cromxt.kafka.BucketObjects;
import com.cromxt.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {


    private final BucketService bucketService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<BucketObjects> getAllBuckets() {
        return bucketService.getAllBuckets();
    }

    @PostMapping
    public ResponseEntity<Void> addBucket(@RequestBody BucketObjects bucketObjects){
        bucketService.createBucket(bucketObjects);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{bucketId}")
    public ResponseEntity<Void> deleteBucket(@PathVariable String bucketId){
        bucketService.deleteBucket(bucketId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping ("/{bucketId}")
    public ResponseEntity<Void> updateBucket(@PathVariable String bucketId,@RequestBody BucketObjects bucketObjects){
        bucketService.updateBucket(bucketId, bucketObjects);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/update/from-server-json")
    public Mono<ResponseEntity<Void>> deleteAllBuckets(){
        bucketService.updateBucketsFromServerJSON();
        return Mono.empty();
    }

    @PostMapping("/from-server-json")
    public Mono<ResponseEntity<GlobalResponse>> addBucketsFromServerJSON(@ModelAttribute FilePart serverJsonFile){
        return bucketService.saveBucketsFromServerJSONFile(serverJsonFile)
                .then(
                        Mono.just(new ResponseEntity<>(GlobalResponse.builder().message("Buckets Added Successfully").build(),
                                HttpStatus.CREATED)
                        )
                )
                .onErrorResume(e->
                        Mono.just(new ResponseEntity<>(GlobalResponse.builder().message(e.getMessage()).build(),
                                HttpStatus.BAD_REQUEST)
                        )
                );
    }

}
