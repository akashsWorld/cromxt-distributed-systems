package com.cromxt.file_handler.service.impl;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import com.cromxt.file_handler.repository.BucketRepository;
import com.cromxt.file_handler.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;

    @Override
    public Flux<BucketRequest> fidAllBuckets() {
        return bucketRepository.findAll().map(bucket -> {
            System.out.println(bucket.getId());
            return new BucketRequest(bucket.getId(), bucket.getHostname(), bucket.getPort());
        });
    }
}
