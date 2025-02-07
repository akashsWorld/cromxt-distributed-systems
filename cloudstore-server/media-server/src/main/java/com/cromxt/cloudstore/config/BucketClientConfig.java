package com.cromxt.cloudstore.config;


import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.cloudstore.clients.impl.BucketGRPCClient;
import com.cromxt.cloudstore.clients.impl.BucketHTTPClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BucketClientConfig {


//    @Bean
//    public BucketClient bucketClient(WebClient.Builder webclient) {
//        return new BucketHTTPClient(webclient);
//    }

    @Bean
    public BucketClient bucketClient() {
        return new BucketGRPCClient();
    }
}
