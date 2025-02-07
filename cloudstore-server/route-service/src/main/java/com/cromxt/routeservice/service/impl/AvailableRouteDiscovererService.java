package com.cromxt.routeservice.service.impl;

import com.cromxt.dtos.service.BucketInformation;
import com.cromxt.dtos.service.BucketsUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AvailableRouteDiscovererService {


    private static final Map<String, AvailableBuckets> ALL_BUCKETS = new HashMap<>();

    public AvailableRouteDiscovererService() {

    }

    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_HEARTBEAT_TOPIC}",containerFactory = "bucketsHeartbeatKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketInformation bucketInformation) {
//        ALL_BUCKETS.put(bucketInformation.getBucketId(),
//                AvailableBuckets.builder()
//                        .hostName(bucketInformation.getBucketHostName())
//                        .port(bucketInformation.getPort())
//                        .initializationTime(System.currentTimeMillis())
//                        .availableSpaceInBytes(bucketInformation.getAvailableSpaceInBytes())
//                        .build());
        System.out.println(bucketInformation);

    }
    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_INFORMATION_UPDATE_TOPIC}",containerFactory = "bucketsUpdateKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketsUpdateRequest bucketUpdateRequest) {
        System.out.println(bucketUpdateRequest);
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
