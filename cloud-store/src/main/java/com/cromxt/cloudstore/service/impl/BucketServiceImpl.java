package com.cromxt.cloudstore.service.impl;

import com.cromxt.dtos.requests.BucketObjects;
import com.cromxt.cloudstore.repository.BucketRepository;
import com.cromxt.cloudstore.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;

    @Override
    public Flux<BucketObjects> fidAllBuckets() {
        return bucketRepository.findAll().map(bucket -> {
            System.out.println(bucket.getId());
            return new BucketObjects(bucket.getId(), bucket.getHostname(), bucket.getPort());
        });
    }
}
