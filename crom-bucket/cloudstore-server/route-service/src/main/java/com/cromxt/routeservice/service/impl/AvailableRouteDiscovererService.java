package com.cromxt.routeservice.service.impl;

import com.cromxt.common.kafka.BucketObject;
import com.cromxt.common.kafka.BucketUpdateRequest;
import com.cromxt.common.kafka.Method;
import com.cromxt.common.kafka.BucketInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
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
        AvailableBuckets availableBucket = ALL_BUCKETS.get(bucketInformation.getBucketId());
        if(availableBucket!=null){
            availableBucket.setAvailableSpaceInBytes(bucketInformation.getAvailableSpaceInBytes());
            availableBucket.setLastRefreshTime(System.currentTimeMillis());
        }else{
            ALL_BUCKETS.put(bucketInformation.getBucketId(), new AvailableBuckets(
                    bucketInformation.getBucketId(),
                    null,
                    null,
                    null,
                    System.currentTimeMillis(),
                    bucketInformation.getAvailableSpaceInBytes()
            ));
        }

    }
    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_INFORMATION_UPDATE_TOPIC}",containerFactory = "bucketsUpdateKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketUpdateRequest bucketUpdateRequest) {
        
        Method method = bucketUpdateRequest.getMethod();
        BucketObject bucketObject = bucketUpdateRequest.getNewBucketObject();

        switch (method) {
            case ADD:
                ALL_BUCKETS.put(bucketObject.getBucketId(), new AvailableBuckets(
                        bucketObject.getBucketId(),
                        bucketObject.getHostName(),
                        bucketObject.getRpcPort(),
                        bucketObject.getHttpPort(),
                        null,
                        null
                ));
                break;
            case UPDATE:
                ALL_BUCKETS.replace(bucketObject.getBucketId(), new AvailableBuckets(
                        bucketObject.getBucketId(),
                        bucketObject.getHostName(),
                        bucketObject.getRpcPort(),
                        bucketObject.getHttpPort(),
                        null,
                        null
                ));
            case DELETE:
                ALL_BUCKETS.remove(bucketObject.getBucketId());
            default:
                break;
        }
        
    }


    @Scheduled(fixedRate =5000)
    public void refreshRoutes(){
        System.out.println(ALL_BUCKETS);
    }


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AvailableBuckets {
        private String bucketId;
        private String hostName;
        private Integer rpcPort;
        private Integer httpPort;
        private Long lastRefreshTime;
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
