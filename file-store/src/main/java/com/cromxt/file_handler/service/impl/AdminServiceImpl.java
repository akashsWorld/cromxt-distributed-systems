package com.cromxt.file_handler.service.impl;

import com.cromxt.file.handler.dtos.requests.BucketRequest;
import com.cromxt.file_handler.clients.GatewayClient;
import com.cromxt.file_handler.entity.Bucket;
import com.cromxt.file_handler.repository.BucketRepository;
import com.cromxt.file_handler.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final BucketRepository bucketRepository;
    private final GatewayClient gatewayClient;
    @Override
    public Mono<String> addBucket(BucketRequest bucketRequest) {

        Bucket bucket = Bucket.builder()
                .hostname(bucketRequest.hostname())
                .port(bucketRequest.port())
                .build();
        return bucketRepository.save(bucket).flatMap(savedBucket->
                gatewayClient
                        .addRoute(new BucketRequest(
                                savedBucket.getId(),
                                savedBucket.getHostname(),
                                savedBucket.getPort()
                        ))
                        .then(Mono.fromCallable(savedBucket::getId))
                        .onErrorResume(ex-> bucketRepository.delete(savedBucket).then(Mono.error(ex)))
        );
    }
}
