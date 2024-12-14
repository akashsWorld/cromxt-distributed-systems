package com.cromxt.file_handler.repository;

import com.cromxt.file_handler.entity.Bucket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BucketRepository extends ReactiveMongoRepository<Bucket, String> {
}
