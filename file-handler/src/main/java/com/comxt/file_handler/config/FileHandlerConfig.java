package com.comxt.file_handler.config;


import com.comxt.client.bucket.BucketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FileHandlerConfig {

    @Bean
    public BucketClient bucketClient(WebClient.Builder builder, Environment environment) {
        return new BucketClient(builder
//                .baseUrl("http://localhost:8080") //Add base path if needed.
                .build(),environment);
    }
}
