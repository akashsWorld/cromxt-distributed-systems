package com.cromxt.bucketserver.service.impl;


import com.cromxt.dtos.service.BucketsUpdateRequest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateBucketKafkaProducer {

    private final String UPDATE_BUCKET_TOPIC;
    private final KafkaTemplate<String, BucketsUpdateRequest> kafkaTemplate;

    public UpdateBucketKafkaProducer(KafkaTemplate<String, BucketsUpdateRequest> kafkaTemplate,
                                     Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.UPDATE_BUCKET_TOPIC = environment.getProperty("BUCKET_SERVER_CONFIG_BUCKET_UPDATE_TOPIC", String.class);
    }

    public Mono<Void> updateBucket(BucketsUpdateRequest bucketsUpdateRequest) {
        return Mono.create((sink) -> {
            try {
                kafkaTemplate.send(UPDATE_BUCKET_TOPIC, bucketsUpdateRequest);
            } catch (Exception e) {
                sink.error(e);
            }
            sink.success();
        });
    }
}
