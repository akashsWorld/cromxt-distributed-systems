package com.cromxt.route_service.service.impl;

import com.cromxt.buckets.BucketInformation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class AvailableRouteDiscovererService {


    private static Map<String,AvailableBuckets> BUCKETS = new HashMap<>();

    public AvailableRouteDiscovererService(Environment environment) throws IOException {
        String filePath = environment.getProperty("BUCKET_SERVICE_AVAILABLE_BUCKET_FILE_PATH", String.class, "D:/Development/projects/crom-bucket/route-service/bucket-information.json");
        Path absolutePath = Paths.get(filePath).toAbsolutePath();
        log.info("Absolute path: {}", absolutePath.toUri());
        ObjectMapper objectMapper = new ObjectMapper();
        BucketData bucketData = objectMapper.readValue(absolutePath.toFile(), BucketData.class);
    }

    @KafkaListener(topics = "buckets")
    public void bucketListUpdated(BucketInformation bucketInformation) {
        BUCKETS.put(bucketInformation.getBucketId(),
                AvailableBuckets.builder()
                        .hostName(bucketInformation.getBucketHostName())
                        .port(bucketInformation.getPort())
                        .initializationTime(System.currentTimeMillis())
                        .availableSpaceInBytes(bucketInformation.getAvailableSpaceInBytes())
                        .build());
        BUCKETS.forEach((key, value) -> log.info("Bucket: {}", value));
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AvailableBuckets {

        private String hostName;
        private Integer port;
        private Long initializationTime;
        private Long availableSpaceInBytes;
    }

//    Needs as JSON parser.
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    private static class BucketData {
//        Add extra metadata if needed.
        List<Buckets> buckets;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class Buckets {
        String bucketId;
        String hostName;
        Integer port;
    }






}
