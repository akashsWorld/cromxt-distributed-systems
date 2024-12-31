package com.cromxt.bucketserver.service.impl;

import com.cromxt.bucketserver.exception.InvalidServerJSONFile;
import com.cromxt.bucketserver.models.Buckets;
import com.cromxt.bucketserver.repository.BucketsRepository;
import com.cromxt.bucketserver.service.BucketService;
import com.cromxt.dtos.requests.BucketObjects;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BucketServiceImpl implements BucketService {

    private final BucketsRepository bucketsRepository;

    @Override
    public Flux<BucketObjects> getAllBuckets() {
       return bucketsRepository.findAll().map(
               buckets -> BucketObjects.builder()
                       .id(buckets.getId())
                       .hostname(buckets.getHostname())
                       .port(buckets.getPort())
                       .build()
       );
    }

    @Override
    public Mono<Void> saveBucketsFromServerJSONFile(FilePart serverJsonFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        return serverJsonFile.content()
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return Mono.just(new String(bytes, StandardCharsets.UTF_8));
                })
                .collectList()
                .map(strings -> String.join("", strings))
                .handle((data, sink) -> {
                    BucketsJSONData bucketsJSONData = null;
                    try {
                        bucketsJSONData = objectMapper.readValue(data, BucketsJSONData.class);
                    } catch (JsonProcessingException e) {
                        sink.error(new RuntimeException(e));
                    }
                    if(bucketsJSONData == null || bucketsJSONData.getBuckets().isEmpty()){
                        sink.error(new InvalidServerJSONFile("Invalid server json file"));
                    }
                    assert bucketsJSONData != null;
                    sink.next(bucketsJSONData.getBuckets());
                })
                .onErrorResume(Mono::error)
                .flatMapMany(bucketEntities
                        -> Flux.fromIterable((List<BucketsEntities>)bucketEntities))
                .map(bucketsEntities -> Buckets.builder()
                        .hostname(bucketsEntities.getHostName())
                        .port(bucketsEntities.getPort())
                        .build())
                .flatMap(bucketsRepository::save)
                .onErrorResume(Mono::error)
                .then(Mono.empty());
    }

    @Override
    public void createBucket(BucketObjects bucketObjects) {

    }

    @Override
    public void deleteBucket(String bucketId) {

    }

    @Override
    public void updateBucket(String bucketId, BucketObjects bucketObjects) {

    }

    @Override
    public void updateBucketsFromServerJSON() {

    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class BucketsJSONData{
        private List<BucketsEntities> buckets;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class BucketsEntities{
        private String hostName;
        private Integer port;
    }
}
