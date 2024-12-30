package com.cromxt.cloudstore.repository;

import com.cromxt.cloudstore.entity.Bucket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BucketRepository extends ReactiveMongoRepository<Bucket, String> {
}
