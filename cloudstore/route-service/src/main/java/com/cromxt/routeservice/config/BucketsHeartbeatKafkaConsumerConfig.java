package com.cromxt.routeservice.config;

import com.cromxt.dtos.service.BucketInformation;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class BucketsHeartbeatKafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, BucketInformation> bucketsHeartbeatConsumerFactory(Environment environment) {
        String bootstrapServers = environment.getProperty("ROUTE_SERVICE_CONFIG_HEARTBEAT_BOOTSTRAP_SERVERS", String.class,"localhost:9092");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, String.format("%s-%s",UUID.randomUUID(), UUID.randomUUID()));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.cromxt.dtos.requests.BucketInformation"); // Ensure correct type
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BucketInformation> bucketsHeartbeatKafkaListenerContainerFactory(Environment environment) {
        ConcurrentKafkaListenerContainerFactory<String, BucketInformation> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bucketsHeartbeatConsumerFactory(environment));
        return factory;
    }
}
