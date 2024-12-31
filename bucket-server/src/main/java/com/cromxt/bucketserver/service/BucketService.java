package com.cromxt.bucketserver.service;

import com.cromxt.kafka.BucketObjects;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BucketService {
    Flux<BucketObjects> getAllBuckets();

    Mono<Void> saveBucketsFromServerJSONFile(FilePart serverJsonFile);

    void createBucket(BucketObjects bucketObjects);

    void deleteBucket(String bucketId);

    void updateBucket(String bucketId, BucketObjects bucketObjects);

    void updateBucketsFromServerJSON();
}
