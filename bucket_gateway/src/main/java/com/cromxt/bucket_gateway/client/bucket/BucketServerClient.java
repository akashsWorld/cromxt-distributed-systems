package com.cromxt.bucket_gateway.client.bucket;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class BucketServerClient {
    Flux<RouteDefinition> getAllAvailableRoutes() {
        return null;
    }
}
