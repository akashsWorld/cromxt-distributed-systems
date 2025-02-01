package com.cromxt.bucketgateway.config;


import com.cromxt.bucketgateway.client.BucketServerClient;
import com.cromxt.bucketgateway.service.impl.DynamicRouteService;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RouterConfiguration {

    @Bean
    public DynamicRouteService dynamicRouteService(BucketServerClient bucketServerClient,
                                                  Environment environment,
                                                  RouteDefinitionWriter routeDefinitionWriter,
                                                  ApplicationEventPublisher applicationEventPublisher) {
        Boolean isSecure =  environment.getProperty("GATEWAY_CONFIG_BUCKETS_PROTOCOL_SECURE", Boolean.class, false);
        return new DynamicRouteService(bucketServerClient,isSecure,routeDefinitionWriter,applicationEventPublisher);
    }

    @Bean
    RouteDefinitionLocator dynamicRouteLocator(DynamicRouteService dynamicRouteService) {
        return dynamicRouteService;
    }

}
