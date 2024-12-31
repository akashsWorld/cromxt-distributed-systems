package com.cromxt.bucketserver.repository;


import com.cromxt.bucketserver.models.Buckets;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BucketsRepository extends ReactiveMongoRepository<Buckets, String> {
}
