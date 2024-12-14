package com.cromxt.bucket_gateway.config;


import com.cromxt.bucket_gateway.client.FileStoreClient;
import com.cromxt.bucket_gateway.service.DynamicRouteService;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RouterConfiguration {

    @Bean
    public DynamicRouteService bucketServerClient(FileStoreClient fileStoreClient, Environment environment){
        Boolean isSecure =  environment.getProperty("BUCKET_GATEWAY.BUCKETS_PROTOCOL", Boolean.class, false);
        return new DynamicRouteService(fileStoreClient,isSecure);
    }

    @Bean
    RouteDefinitionLocator dynamicRouteLocator(DynamicRouteService dynamicRouteService) {
        return dynamicRouteService;
    }

}
