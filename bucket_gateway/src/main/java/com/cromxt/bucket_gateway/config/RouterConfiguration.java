package com.cromxt.bucket_gateway.config;


import com.cromxt.bucket_gateway.client.bucket.BucketServerClient;
import com.cromxt.bucket_gateway.service.DynamicRouteService;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfiguration {

    @Bean
    RouteDefinitionLocator dynamicRouteLocator(BucketServerClient bucketServerClient){
        return new DynamicRouteService(bucketServerClient);
    }

}
