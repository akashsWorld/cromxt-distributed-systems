package com.cromxt.bucket.service;


import com.cromxt.buckets.BucketInformation;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BucketInformationEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;
    private final String storagePath;

    public BucketInformationEventProducer(KafkaTemplate<String, String> kafkaTemplate, Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = environment.getProperty("BUCKET_SERVICE.KAFKA_TOPIC_NAME", String.class, "buckets");
        this.storagePath = environment.getProperty("BUCKET_SERVICE.STORAGE_PATH", String.class, "C:/Users/akash/Downloads/root_bucket");
    }

    @Scheduled(fixedRate = 15000)
    public void sendStorageInformation(){
        kafkaTemplate.send(topic, bucketInformation().toString());
    }

    private BucketInformation bucketInformation(){
        File rootDirectory = new File(storagePath);
        long availableSpaceInBytes = rootDirectory.getFreeSpace();
        return new BucketInformation("some-bucket-id", availableSpaceInBytes);
    }

}
