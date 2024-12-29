package com.cromxt.bucketserver.service;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BucketService {
    List<BucketRequest> getAllBuckets();

    void saveBucketsFromServerJSONFile(MultipartFile serverJsonFile);

    void createBucket(BucketRequest bucketRequest);

    void deleteBucket(String bucketId);

    void updateBucket(String bucketId, BucketRequest bucketRequest);

    void updateBucketsFromServerJSON();
}
