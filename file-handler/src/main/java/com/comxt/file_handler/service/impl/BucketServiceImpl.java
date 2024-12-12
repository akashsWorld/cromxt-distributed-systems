package com.comxt.file_handler.service.impl;

import com.comxt.client.bucket.BucketClient;
import com.comxt.file_handler.dtos.request.AddBucketRequest;
import com.comxt.file_handler.entity.Buckets;
import com.comxt.file_handler.repository.BucketsRepository;
import com.comxt.file_handler.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketsRepository bucketsRepository;
    private final BucketClient bucketClient;


    @Override
    public Mono<Void> addBucket(AddBucketRequest addBucketRequest) {
        return bucketClient.getAvailableSpace(addBucketRequest.bucketHostName(),addBucketRequest.bucketPort()).flatMap(
                availableSpace -> {
                    Buckets buckets = Buckets.builder()
                            .bucketName(addBucketRequest.bucketName())
                            .availableSpace(availableSpace)
                            .serverPort(addBucketRequest.bucketPort())
                            .serverHostName(addBucketRequest.bucketHostName())
                            .build();
                        return bucketsRepository.save(buckets).then().onErrorResume(e -> {
                            System.out.println(e.toString());
                        return Mono.error(e);
                    });
                }
        );
    }
}
