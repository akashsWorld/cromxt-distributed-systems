package com.cromxt.bucketserver.controller;


import com.cromxt.bucketserver.service.BucketService;
import com.cromxt.file.handler.dtos.requests.BucketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {


    private final BucketService bucketService;


    @GetMapping
    public ResponseEntity<List<BucketRequest>> getAllBuckets() {
        return ResponseEntity.ok(bucketService.getAllBuckets());
    }

    @PostMapping
    public ResponseEntity<Void> addBucket(@RequestBody BucketRequest bucketRequest){
        bucketService.createBucket(bucketRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{bucketId}")
    public ResponseEntity<Void> deleteBucket(@PathVariable String bucketId){
        bucketService.deleteBucket(bucketId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping ("/{bucketId}")
    public ResponseEntity<Void> updateBucket(@PathVariable String bucketId,@RequestBody BucketRequest bucketRequest){
        bucketService.updateBucket(bucketId,bucketRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/update/from-server-json")
    public ResponseEntity<Void> deleteAllBuckets(){
        bucketService.updateBucketsFromServerJSON();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/from-server-json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addBucketsFromServerJSON(@ModelAttribute MultipartFile serverJsonFile){
        bucketService.saveBucketsFromServerJSONFile(serverJsonFile);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
