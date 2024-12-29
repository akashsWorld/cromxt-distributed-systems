package com.cromxt.bucketserver.service;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BucketService {
    List<BucketRequest> getAllBuckets();

    void saveBucketsFromServerJSONFile(MultipartFile serverJsonFile);
}
