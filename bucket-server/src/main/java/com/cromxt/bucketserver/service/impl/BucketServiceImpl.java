package com.cromxt.bucketserver.service.impl;

import com.cromxt.bucketserver.exception.InvalidServerJSONFile;
import com.cromxt.bucketserver.models.Buckets;
import com.cromxt.bucketserver.repository.BucketsRepository;
import com.cromxt.bucketserver.service.BucketService;
import com.cromxt.file.handler.dtos.requests.BucketRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BucketServiceImpl implements BucketService {

    private final BucketsRepository bucketsRepository;

    @Override
    public List<BucketRequest> getAllBuckets() {
        List<Buckets> buckets = bucketsRepository.findAll();
        return buckets.stream().map(eachBucket-> BucketRequest.builder()
                .id(eachBucket.getId())
                .hostname(eachBucket.getHostname())
                .port(eachBucket.getPort())
                .build()
        ).toList();
    }

    @Override
    public void saveBucketsFromServerJSONFile(MultipartFile serverJsonFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        final BucketsJSONData bucketsJSONData;
        try{
            File file = serverJsonFile.getResource().getFile();
            bucketsJSONData = objectMapper.readValue(file,BucketsJSONData.class);
        }
        catch (IOException ioException){
            log.error("Unable to parse the json data {}",ioException.getMessage());
            throw new InvalidServerJSONFile(ioException.getMessage());
        }

        List<Buckets> bucketsList = bucketsJSONData.buckets.stream().map(bucketsEntities ->
                Buckets.builder()
                        .hostname(bucketsEntities.getHostName())
                        .port(bucketsEntities.getPort())
                        .build()).toList();

        bucketsRepository.saveAll(bucketsList);
    }

    @Override
    public void createBucket(BucketRequest bucketRequest) {

    }

    @Override
    public void deleteBucket(String bucketId) {

    }

    @Override
    public void updateBucket(String bucketId, BucketRequest bucketRequest) {

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
