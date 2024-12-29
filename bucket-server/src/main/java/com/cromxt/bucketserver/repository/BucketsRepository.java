package com.cromxt.bucketserver.repository;


import com.cromxt.bucketserver.models.Buckets;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BucketsRepository extends MongoRepository<Buckets, String> {
}
