package com.cromxt.bucketserver.service.impl;

import com.cromxt.bucketserver.client.ServerClient;
import com.cromxt.bucketserver.exception.InvalidBucketDetails;
import com.cromxt.bucketserver.exception.InvalidServerJSONFile;
import com.cromxt.bucketserver.models.Buckets;
import com.cromxt.bucketserver.repository.BucketsRepository;
import com.cromxt.bucketserver.service.BucketService;
import com.cromxt.dtos.client.requests.NewBucketRequest;
import com.cromxt.dtos.client.response.BucketResponseDTO;
import com.cromxt.dtos.client.response.derived.NewBucketResponse;
import com.cromxt.dtos.service.BucketObject;
import com.cromxt.dtos.service.BucketsUpdateRequest;
import com.cromxt.dtos.service.Method;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BucketServiceImpl implements BucketService {

    private final BucketsRepository bucketsRepository;
    private final ServerClient serverClient;
    private final UpdateBucketKafkaProducer updateBucketKafkaProducer;

    @Override
    public Flux<BucketResponseDTO> findAllBuckets() {
       return bucketsRepository.findAll().map(
               buckets -> new BucketResponseDTO(
                       buckets.getId(),
                       buckets.getHostname(),
                       buckets.getPort()
               )
       );

    }

    @Override
    public Mono<NewBucketResponse> saveBucketsFromServerJSONFile(FilePart serverJsonFile) {
//        TODO: Implement later
        return Mono.empty();
    }

    @Override
    public Mono<BucketResponseDTO> createBucket(NewBucketRequest newBucketRequest) {
        Mono<Buckets> savedBucket = bucketsRepository.save(Buckets.builder()
                .hostname(newBucketRequest.getHostname())
                .port(newBucketRequest.getPort())
                .build())
                .onErrorResume(Mono::error)
                .flatMap(bucket-> updateBucketKafkaProducer.updateBucket(
                        new BucketsUpdateRequest(
                                Method.ADD,
                                List.of(new BucketObject(bucket.getId(),bucket.getHostname(),bucket.getPort()))
                        )
                ).then(Mono.just(bucket)));
        return savedBucket.flatMap(bucket->{
            String bucketId = bucket.getId();
            return serverClient
                    .launchInstance(
                            "http",
                            bucketId,
                            newBucketRequest.getHostname(),
                            newBucketRequest.getPort()
                    ).then(Mono.just(new BucketResponseDTO(bucketId,bucket.getHostname(),bucket.getPort())));
        });
    }

    @Override
    public Mono<Void> deleteBucketById(String bucketId) {
        return bucketsRepository.findById(bucketId)
                .switchIfEmpty(Mono.error(new InvalidBucketDetails("Bucket not found")))
                .flatMap(bucket->bucketsRepository.delete(bucket).then(updateBucketKafkaProducer.updateBucket(
                        new BucketsUpdateRequest(
                                Method.DELETE,
                                List.of(new BucketObject(bucketId,bucket.getHostname(),bucket.getPort()))
                        )
                )));
    }

    @Override
    public Mono<Void> updateBucket(String bucketId, NewBucketRequest newBucketRequest) {

        return bucketsRepository.findById(bucketId)
                .flatMap(bucket->{
                  if(!Objects.isNull(bucket.getHostname()) && bucket.getHostname().length()>7){
                      bucket.setHostname(newBucketRequest.getHostname());
                  }
                  if(!Objects.isNull(bucket.getPort()) && bucket.getPort() > 0){
                      bucket.setPort(newBucketRequest.getPort());
                  }
                  return bucketsRepository.save(bucket);
                })
                .flatMap(buckets -> updateBucketKafkaProducer.updateBucket(new BucketsUpdateRequest(
                        Method.UPDATE,
                        List.of(new BucketObject(buckets.getId(),buckets.getHostname(),buckets.getPort()))
                )))
                .then();
    }



    @Override
    public Mono<Void> updateBucketsFromServerJSON() {
//        TODO: Implement later.
        return null;
    }

    @Override
    public Mono<Void> deleteAllBuckets() {
        return bucketsRepository.findAll()
                .map(buckets -> new BucketObject(buckets.getId(),buckets.getHostname(),buckets.getPort()))
                .collectList()
                .flatMap(bucketObjects -> bucketsRepository.deleteAll()
                        .then(updateBucketKafkaProducer.updateBucket(new BucketsUpdateRequest(Method.DELETE,bucketObjects)))
                );
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

    private <T> Mono<T> parseServerJsonFile(FilePart jsonFile,Class<T> parseIn){
        ObjectMapper objectMapper = new ObjectMapper();
        return jsonFile.content()
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return Mono.just(new String(bytes, StandardCharsets.UTF_8));
                })
                .collectList()
                .map(strings -> String.join("", strings))
                .mapNotNull(dataString -> {
                    try {
                        return objectMapper.readValue(dataString, parseIn);
                    } catch (JsonProcessingException e) {
                        throw new InvalidServerJSONFile(e.getMessage());
                    }
                });
    }



}
