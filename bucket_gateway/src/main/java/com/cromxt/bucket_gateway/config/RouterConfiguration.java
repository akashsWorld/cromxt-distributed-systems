package com.cromxt.bucket_gateway.config;


import com.cromxt.bucket_gateway.client.bucket.BucketServerClient;
import com.cromxt.bucket_gateway.service.DynamicRouteService;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.List;

@Configuration
public class RouterConfiguration {

    @Bean
    RouteDefinitionLocator dynamicRouteLocator(BucketServerClient bucketServerClient){
       DynamicRouteService locator = new DynamicRouteService(bucketServerClient);
       RouteDefinition routeDefinition = new RouteDefinition();
       routeDefinition.setId("bucket1");
       routeDefinition.setPredicates(List.of(
               new PredicateDefinition("Method=POST"),
               new PredicateDefinition("Path=/server")
       ));
       routeDefinition.setFilters(List.of(
               new FilterDefinition("RewritePath=/server, /api/v1/files")
       ));
       routeDefinition.setUri(URI.create("http://127.0.0.1:8090/api/v1/files"));
       locator.addRoute(
               routeDefinition
       );
       return locator;
    }

}
