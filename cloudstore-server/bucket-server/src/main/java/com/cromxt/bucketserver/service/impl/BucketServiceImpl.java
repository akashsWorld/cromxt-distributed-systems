package com.cromxt.bucketserver.service.impl;

import com.cromxt.bucketserver.client.ServerClient;
import com.cromxt.bucketserver.exception.InvalidBucketDetails;
import com.cromxt.bucketserver.exception.InvalidServerJSONFile;
import com.cromxt.bucketserver.models.Buckets;
import com.cromxt.bucketserver.repository.BucketsRepository;
import com.cromxt.bucketserver.service.BucketService;
import com.cromxt.common.requests.client.requests.NewBucketRequest;
import com.cromxt.common.requests.client.response.BucketResponseDTO;
import com.cromxt.common.requests.client.response.derived.NewBucketResponse;
import com.cromxt.common.requests.service.BucketObject;
import com.cromxt.common.requests.service.BucketUpdateRequest;
import com.cromxt.common.requests.service.Method;
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
    private final ServerClient serverClient;
    private final UpdateBucketKafkaProducer updateBucketKafkaProducer;

    @Override
    public Flux<BucketResponseDTO> findAllBuckets() {
        return bucketsRepository.findAll().map(
                buckets -> new BucketResponseDTO(
                        buckets.getId(),
                        buckets.getHostname(),
                        buckets.getHttpPort(),
                        buckets.getRpcPort()
                )
        );

    }

    @Override
    public Mono<NewBucketResponse> saveBucketsFromServerJSONFile(FilePart serverJsonFile) {
        // TODO: Implement later
        return Mono.empty();
    }

    @Override
    public Mono<BucketResponseDTO> createBucket(NewBucketRequest newBucketRequest) {
        String hostName = newBucketRequest.getHostName();
        Buckets buckets = Buckets.builder()
                .hostname(hostName)
                .build();

        return bucketsRepository.save(buckets)
                .flatMap(savedBucket->{

                    String bucketId = savedBucket.getId();

                    return serverClient.launchNewBucket(bucketId, hostName, newBucketRequest.getPort())
                            .flatMap(launchedInstance -> {

                                savedBucket.setHttpPort(launchedInstance.httpPort());
                                savedBucket.setRpcPort(launchedInstance.rpcPort());

                                return bucketsRepository.save(savedBucket).flatMap(updatedBucket -> {

                                    updateBucketKafkaProducer.updateBucket(new BucketUpdateRequest(
                                            Method.ADD,
                                            BucketObject.builder()
                                                    .hostName(hostName)
                                                    .httpPort(updatedBucket.getHttpPort())
                                                    .rocPort(updatedBucket.getHttpPort())
                                                    .build()
                                    ));

                                    return Mono.just(new BucketResponseDTO(
                                            savedBucket.getId(),
                                            savedBucket.getHostname(),
                                            savedBucket.getHttpPort(),
                                            savedBucket.getRpcPort()));
                                });

                            });

                });

    }

    @Override
    public Mono<Void> deleteBucketById(String bucketId) {
        return bucketsRepository.findById(bucketId)
                .switchIfEmpty(Mono.error(new InvalidBucketDetails("Bucket not found")))
                .flatMap(bucket -> bucketsRepository.delete(bucket).then(updateBucketKafkaProducer.updateBucket(
                        new BucketUpdateRequest(
                                Method.DELETE,
                                BucketObject.builder()
                                .bucketId(bucketId)
                                .build()
                                ))));
    }

    @Override
    public Mono<BucketResponseDTO> updateBucket(String bucketId, NewBucketRequest newBucketRequest) {
        String hostName = newBucketRequest.getHostName();
        return bucketsRepository.findById(bucketId)
        .flatMap(savedBucket-> serverClient.launchNewBucket(bucketId, hostName,newBucketRequest.getPort())
        .flatMap(launchedInstance->{

            savedBucket.setHostname(hostName);
            savedBucket.setRpcPort(launchedInstance.rpcPort());
            savedBucket.setHttpPort(launchedInstance.httpPort());

            return bucketsRepository.save(savedBucket).map(updatedBucket->{

                updateBucketKafkaProducer.updateBucket(
                        new BucketUpdateRequest(
                                Method.UPDATE,
                                BucketObject.builder()
                                        .hostName(hostName)
                                        .httpPort(updatedBucket.getHttpPort())
                                        .rocPort(updatedBucket.getRpcPort())
                                        .build()
                        )
                );

                return new BucketResponseDTO(
                        bucketId,
                        hostName,
                        updatedBucket.getHttpPort(),
                        updatedBucket.getRpcPort()
                );

            });
        }));
    }

    @Override
    public Mono<Void> updateBucketsFromServerJSON() {
        // TODO: Implement later.
        return null;
    }

    @Override
    public Mono<Void> deleteAllBuckets() {
        return bucketsRepository.findAll().doOnNext(bukcetObject -> {
            updateBucketKafkaProducer.updateBucket(new BucketUpdateRequest(
                    Method.DELETE,
                    BucketObject.builder().bucketId(bukcetObject.getId()).build()
            ));
        }).then(bucketsRepository.deleteAll());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class BucketsJSONData {
        private List<BucketsEntities> buckets;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class BucketsEntities {
        private String hostName;
        private Integer port;
    }

    private <T> Mono<T> parseServerJsonFile(FilePart jsonFile, Class<T> parseIn) {
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
