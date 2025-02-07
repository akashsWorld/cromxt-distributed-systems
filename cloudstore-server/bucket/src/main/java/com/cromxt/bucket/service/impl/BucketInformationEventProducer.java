package com.cromxt.bucket.service.impl;


import com.cromxt.dtos.service.BucketInformation;
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
    private final String bucketId;

    public BucketInformationEventProducer(
            KafkaTemplate<String, BucketInformation> kafkaTemplate,
            Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = environment.getProperty("BUCKET_CONFIG_KAFKA_TOPIC_NAME", String.class, "buckets");
        this.storagePath = environment.getProperty("BUCKET_CONFIG_STORAGE_PATH", String.class, "C:/Users/akash/Downloads/root_bucket");
        this.bucketId = environment.getProperty("BUCKET_CONFIG_ID", String.class);

    }

    @Scheduled(fixedRate = 15000)
    public void sendStorageInformation(){
        kafkaTemplate.send(topic, getBucketInFormation());
    }

    private BucketInformation getBucketInFormation(){
        File rootDirectory = new File(storagePath);
        long availableSpaceInBytes = rootDirectory.getFreeSpace();
        return BucketInformation.builder()
                .bucketId(bucketId)
                .availableSpaceInBytes(availableSpaceInBytes)
                .build();
    }

}
