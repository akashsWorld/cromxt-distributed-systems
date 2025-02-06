package com.cromxt.cloudstore.repository;

import com.cromxt.cloudstore.entity.MediaObjects;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface MediaRepository extends ReactiveMongoRepository<MediaObjects, String> {
    Mono<MediaObjects> findByName(String name);
}
