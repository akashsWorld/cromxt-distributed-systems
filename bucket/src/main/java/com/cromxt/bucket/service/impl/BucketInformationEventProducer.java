package com.cromxt.bucket.service.impl;


import com.cromxt.dtos.requests.BucketInformation;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BucketInformationEventProducer {

    private final KafkaTemplate<String, BucketInformation> kafkaTemplate;
    private final String topic;
    private final String storagePath;
//    Static methods
    private static String APPLICATION_HOSTNAME;
    private static Integer APPLICATION_PORT;
    private static String UNIQUE_BUCKET_ID;

    public BucketInformationEventProducer(
            KafkaTemplate<String, BucketInformation> kafkaTemplate,
            Environment environment,
            ApplicationHostNetworkFinder applicationHostNetworkFinder) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = environment.getProperty("BUCKET_CONFIG_KAFKA_TOPIC_NAME", String.class, "buckets");
        this.storagePath = environment.getProperty("BUCKET_CONFIG_STORAGE_PATH", String.class, "C:/Users/akash/Downloads/root_bucket");

        APPLICATION_HOSTNAME = applicationHostNetworkFinder.getApplicationHostname();
        APPLICATION_PORT = applicationHostNetworkFinder.getApplicationPort();
        UNIQUE_BUCKET_ID = environment.getProperty("BUCKET_CONFIG_ID", String.class, "1");
    }

    @Scheduled(fixedRate = 15000)
    public void sendStorageInformation(){
        kafkaTemplate.send(topic, getBucketInFormation());
    }

    private BucketInformation getBucketInFormation(){
        File rootDirectory = new File(storagePath);
        long availableSpaceInBytes = rootDirectory.getFreeSpace();
        return BucketInformation.builder()
                .bucketId(UNIQUE_BUCKET_ID)
                .bucketHostName(APPLICATION_HOSTNAME)
                .port(APPLICATION_PORT)
                .availableSpaceInBytes(availableSpaceInBytes)
                .build();
    }

}
