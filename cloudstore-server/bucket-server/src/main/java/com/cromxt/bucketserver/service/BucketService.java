package com.cromxt.bucketserver.service;

import com.cromxt.common.requests.client.requests.NewBucketRequest;
import com.cromxt.common.requests.client.response.BucketResponseDTO;
import com.cromxt.common.requests.client.response.derived.NewBucketResponse;
import com.cromxt.dtos.client.requests.NewBucketRequest;
import com.cromxt.dtos.client.response.BucketResponseDTO;
import com.cromxt.dtos.client.response.derived.NewBucketResponse;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BucketService {
    Flux<BucketResponseDTO> findAllBuckets();

    Mono<NewBucketResponse> saveBucketsFromServerJSONFile(FilePart serverJsonFile);

    Mono<BucketResponseDTO> createBucket(NewBucketRequest newBucketRequest);

    Mono<Void> deleteBucketById(String bucketId);

    Mono<BucketResponseDTO> updateBucket(String bucketId, NewBucketRequest newBucketRequest);

    Mono<Void> updateBucketsFromServerJSON();

    Mono<Void> deleteAllBuckets();
}
