package com.cromxt.bucketserver.controller;


import com.cromxt.bucketserver.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/buckets")
@RequiredArgsConstructor
public class BucketController {


    private final BucketService bucketService;
}
