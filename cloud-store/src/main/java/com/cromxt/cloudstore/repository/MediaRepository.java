package com.cromxt.cloudstore.repository;

import com.cromxt.cloudstore.entity.MediaObjects;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MediaRepository extends ReactiveMongoRepository<MediaObjects, String> {
}
