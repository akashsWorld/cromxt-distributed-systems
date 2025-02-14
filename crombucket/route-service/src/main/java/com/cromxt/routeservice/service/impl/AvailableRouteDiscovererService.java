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


    private static final Map<String, AvailableBucket> AVAILABLE_BUCKETS = new HashMap<>();
    private static final Map<String, AvailableBucket> ONLINE_BUCKETS = new HashMap<>();
    private static String lastUsedBucketId = null;
    private static String bucketHaveLargerSpaceTillNow = null;
    private static Queue<String> bucketQue = new LinkedList<>();
    private final int bucketLoadCount;
    private static int bucketCount = 0;


    public AvailableRouteDiscovererService(Environment environment) {
        Integer loadFactor = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_LOAD_COUNT", Integer.class);
        assert loadFactor != null;
        this.bucketLoadCount = loadFactor;
    }

    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_HEARTBEAT_TOPIC}", containerFactory = "bucketsHeartbeatKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketHeartBeat bucketHeartBeat) {

        String bucketId = bucketHeartBeat.getBucketId();
        if (AVAILABLE_BUCKETS.containsKey(bucketId)) {
            AvailableBucket bucket = ONLINE_BUCKETS.get(bucketId);
            if (bucket == null) {
                bucket = AVAILABLE_BUCKETS.get(bucketId);
                bucketQue.add(bucketId);
            }
            bucket.setLastRefreshTime(System.currentTimeMillis());
            bucket.setAvailableSpaceInBytes(bucketHeartBeat.getAvailableSpaceInBytes());
            ONLINE_BUCKETS.put(bucketId, bucket);
            if(bucketHaveLargerSpaceTillNow== null){
                bucketHaveLargerSpaceTillNow = bucketId;
            }
            Long largeSpace = ONLINE_BUCKETS.get(bucketHaveLargerSpaceTillNow).getAvailableSpaceInBytes();
            if(largeSpace ==null){
                return;
            }
            bucketHaveLargerSpaceTillNow = largeSpace > bucketHeartBeat.getAvailableSpaceInBytes() ? bucketHaveLargerSpaceTillNow : bucketId;
        }
    }

    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_INFORMATION_UPDATE_TOPIC}", containerFactory = "bucketsUpdateKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketUpdateRequest bucketUpdateRequest) {

        Method method = bucketUpdateRequest.getMethod();
        BucketObject bucketObject = bucketUpdateRequest.getNewBucketObject();

        switch (method) {
            case ADD:
                System.out.println("Executed");
                AVAILABLE_BUCKETS.put(bucketObject.getBucketId(),
                        new AvailableBucket(
                                bucketObject.getBucketId(),
                                bucketObject.getHostName(),
                                bucketObject.getRpcPort(),
                                bucketObject.getHttpPort(),
                                System.currentTimeMillis(),
                                null)
                );
                break;
            case UPDATE:
                AVAILABLE_BUCKETS.replace(bucketObject.getBucketId(),
                        new AvailableBucket(
                                bucketObject.getBucketId(),
                                bucketObject.getHostName(),
                                bucketObject.getRpcPort(),
                                bucketObject.getHttpPort(),
                                System.currentTimeMillis(),
                                null));
            case DELETE:
                AVAILABLE_BUCKETS.remove(bucketObject.getBucketId());
            default:
                break;
        }

    }

    @Scheduled(fixedRate = 5000)
    public void printBuckets(){
        System.out.println(ONLINE_BUCKETS);
    }


    @Scheduled(fixedRate = 10000)
    public void refreshRoutes() {
        ONLINE_BUCKETS.forEach((bucketId, bucketDetails) -> {
            if (bucketDetails.getLastRefreshTime() + 15000 < System.currentTimeMillis()) {
                ONLINE_BUCKETS.remove(bucketId);
            }
        });
    }

    public Mono<BucketDetails> getBucket(MediaDetails mediaDetails) {

        if (bucketQue.isEmpty()) {
            return Mono.error(new BucketError("No registered buckets found."));
        }


//        To manage the space in all buckets this code sends each second request to the bucket with larger space.
        if (bucketCount == bucketLoadCount && (!Objects.equals(lastUsedBucketId, bucketHaveLargerSpaceTillNow) || AVAILABLE_BUCKETS.size() == 1) && ONLINE_BUCKETS.containsKey(bucketHaveLargerSpaceTillNow)) {
            AvailableBucket availableBucket = AVAILABLE_BUCKETS.get(bucketHaveLargerSpaceTillNow);
            lastUsedBucketId = bucketHaveLargerSpaceTillNow;
            bucketCount++;
            return Mono.just(BucketDetails.builder()
                    .bucketId(availableBucket.getBucketId())
                    .rpcPort(availableBucket.getRpcPort())
                    .httpPort(availableBucket.getHttpPort())
                    .hostName(availableBucket.getHostName())
                    .build());
        }

        String generatedBucketId = null;


        String tempBucketId = null;
        while (!bucketQue.isEmpty()) {
            tempBucketId = bucketQue.poll();
            if (ONLINE_BUCKETS.containsKey(tempBucketId)) {
                generatedBucketId = tempBucketId;
                bucketQue.add(tempBucketId);
                break;
            }
        }

        if (generatedBucketId == null) { // Case 1
            return Mono.error(new BucketError("No registered buckets found."));
        }

        AvailableBucket bucket = ONLINE_BUCKETS.get(generatedBucketId);



        return Mono.just(BucketDetails.builder()
                        .hostName(bucket.getHostName())
                        .bucketId(bucket.getBucketId())
                        .httpPort(bucket.getHttpPort())
                        .rpcPort(bucket.getRpcPort())
                .build());
    }


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AvailableBucket {
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
