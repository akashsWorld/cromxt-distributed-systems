package com.cromxt.bucketgateway.filter;

import com.cromxt.bucketgateway.client.RouteServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class RouteFilter implements GlobalFilter, Ordered {

    private final RouteServiceClient routeServiceClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        System.out.println("Request: " + request.getMethod() + " " + path);
//        return getFileSize(exchange)
//                .flatMap(fileSize->{
//                    System.out.println(fileSize);
//                  return chain.filter(exchange);
//                });
        return chain.filter(exchange);
    }

    private Mono<Long> getFileSize(ServerWebExchange exchange) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        return serverHttpRequest
                .getBody()
                .map(dataBuffer -> (long)dataBuffer.readableByteCount())
                .reduce(Long::sum);
    }



    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
