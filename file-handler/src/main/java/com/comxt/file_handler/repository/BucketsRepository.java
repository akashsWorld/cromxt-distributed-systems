package com.comxt.file_handler.repository;

import com.comxt.file_handler.entity.Buckets;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketsRepository extends ReactiveMongoRepository<Buckets, String> {
}
