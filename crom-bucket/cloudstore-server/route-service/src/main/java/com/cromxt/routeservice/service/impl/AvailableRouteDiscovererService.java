package com.cromxt.routeservice.service.impl;

import com.cromxt.common.kafka.BucketObject;
import com.cromxt.common.kafka.BucketUpdateRequest;
import com.cromxt.common.kafka.Method;
import com.cromxt.common.kafka.BucketHeartBeat;
import com.cromxt.common.routeing.BucketDetails;
import com.cromxt.common.routeing.MediaDetails;
import com.cromxt.routeservice.exception.BucketError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Service
public class AvailableRouteDiscovererService {


    private static final Map<String, AvailableBuckets> ALL_BUCKETS = new HashMap<>();
    private static String lastUsedBucketId = null;
    private static AvailableBuckets  bucketHaveLargerSpaceTillNow = null;
    private static Queue<String> bucketQue = new LinkedList<>();
    private final int bucketLoadCount;
    private static int bucketCount = 0;


    public AvailableRouteDiscovererService(Environment environment) {
        Integer loadFactor = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_LOAD_COUNT", Integer.class);
        assert loadFactor != null;
        this.bucketLoadCount = loadFactor;
    }

    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_HEARTBEAT_TOPIC}",containerFactory = "bucketsHeartbeatKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketHeartBeat bucketHeartBeat) {
        AvailableBuckets availableBucket = ALL_BUCKETS.get(bucketHeartBeat.getBucketId());
        final AvailableBuckets currentBucket;
        if(availableBucket!=null){
            availableBucket.setAvailableSpaceInBytes(bucketHeartBeat.getAvailableSpaceInBytes());
            availableBucket.setLastRefreshTime(System.currentTimeMillis());
            currentBucket = availableBucket;
        }else{
            AvailableBuckets newBucket = new AvailableBuckets(
                    bucketHeartBeat.getBucketId(),
                    null,
                    null,
                    null,
                    System.currentTimeMillis(),
                    bucketHeartBeat.getAvailableSpaceInBytes()
            );
            ALL_BUCKETS.put(bucketHeartBeat.getBucketId(),newBucket);
            currentBucket = newBucket;
            bucketQue.add(newBucket.getBucketId());
        }

        if(currentBucket.getAvailableSpaceInBytes() > bucketHaveLargerSpaceTillNow.getAvailableSpaceInBytes()){
            bucketHaveLargerSpaceTillNow = currentBucket;
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


    @Scheduled(fixedRate =10000)
    public void refreshRoutes(){
        ALL_BUCKETS.forEach((bucketId,bucketDetails)->{
            if(bucketDetails.getLastRefreshTime()+15000<System.currentTimeMillis()){
                ALL_BUCKETS.remove(bucketId);
            }
        });
    }

    public Mono<BucketDetails> getBucket(MediaDetails mediaDetails){

        String generatedBucketId = bucketQue.remove();

        while (!ALL_BUCKETS.containsKey(generatedBucketId)){
            generatedBucketId = bucketQue.remove();
        }

        if(generatedBucketId==null){
            return Mono.error(new BucketError("No registered buckets found."));
        }

        if(generatedBucketId.equals(lastUsedBucketId) && bucketQue.size()==1 && ALL_BUCKETS.containsKey(generatedBucketId)){
            AvailableBuckets bucketDetails = ALL_BUCKETS.get(generatedBucketId);
            bucketQue.add(generatedBucketId);
            return Mono.just(
                    BucketDetails.builder()
                            .bucketId(bucketDetails.getBucketId())
                            .hostName(bucketDetails.getHostName())
                            .httpPort(bucketDetails.getHttpPort())
                            .rpcPort(bucketDetails.getRpcPort())
                            .build()
            );
        }


        return Mono.just(
                BucketDetails.builder()
                        .bucketId(bucketDetails.getBucketId())
                        .hostName(bucketDetails.getHostName())
                        .httpPort(bucketDetails.getHttpPort())
                        .rpcPort(bucketDetails.getRpcPort())
                        .build()
        );
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
