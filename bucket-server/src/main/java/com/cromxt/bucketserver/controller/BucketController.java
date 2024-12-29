package com.cromxt.bucketserver.controller;


import com.cromxt.bucketserver.service.BucketService;
import com.cromxt.file.handler.dtos.requests.BucketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buckets")
@RequiredArgsConstructor
public class BucketController {


    private final BucketService bucketService;


    @GetMapping
    public ResponseEntity<List<BucketRequest>> getAllBuckets() {
        return ResponseEntity.ok(bucketService.getAllBuckets());
    }

    @PostMapping("/add/from-server-json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createBucketsFromServerJsonFile(@ModelAttribute MultipartFile serverJsonFile){
        bucketService.saveBucketsFromServerJSONFile(serverJsonFile);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
