package com.cromxt.system.service;

import com.cromxt.system.dtos.NewBucketRequest;
import com.cromxt.system.dtos.SavedBucketRespnse;
import com.cromxt.system.dtos.response.BucketListResponse;
import com.cromxt.system.dtos.response.BucketResponse;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BucketService {
    Mono<BucketListResponse> findAllBuckets();

    Mono<BucketListResponse> saveBucketsFromServerJSONFile(FilePart serverJsonFile);

    Mono<BucketResponse> createBucket(NewBucketRequest newBucketRequest);

    Mono<Void> deleteBucketById(String bucketId);

    Mono<BucketResponse> updateBucket(String bucketId, NewBucketRequest newBucketRequest);

    Mono<Void> updateBucketsFromServerJSON();

    Mono<Void> deleteAllBuckets();
}
