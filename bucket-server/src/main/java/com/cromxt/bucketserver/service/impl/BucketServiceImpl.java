package com.cromxt.bucketserver.service.impl;

import com.cromxt.bucketserver.models.Buckets;
import com.cromxt.bucketserver.repository.BucketsRepository;
import com.cromxt.bucketserver.service.BucketService;
import com.cromxt.file.handler.dtos.requests.BucketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketsRepository bucketsRepository;

    @Override
    public List<BucketRequest> getAllBuckets() {
        List<Buckets> buckets = bucketsRepository.findAll();
        return buckets.stream().map(eachBucket-> BucketRequest.builder()
                .id(eachBucket.getId())
                .hostname(eachBucket.getHostname())
                .port(eachBucket.getPort())
                .build()
        ).toList();
    }
}
