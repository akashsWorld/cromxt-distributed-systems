package com.cromxt.bucket.config;


import com.cromxt.bucket.interceptors.MediaHandlerInterceptor;
import com.cromxt.bucket.service.impl.ApplicationHostNetworkFinder;
import com.cromxt.bucket.service.impl.MediaHandlerGRPCServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.InetSocketAddress;
@Slf4j
@Configuration
public class GrpcServerConfig {

    @Bean
    public Server mediaHandlerGrpcServer(MediaHandlerGRPCServiceImpl mediaHandlerGRPCService,
                                         ApplicationHostNetworkFinder applicationHostNetworkFinder) throws IOException {

        Integer grpcPort = applicationHostNetworkFinder.getApplicationPort();
        String grpcHost = applicationHostNetworkFinder.getApplicationHostname();
        assert grpcPort != null;
        assert grpcHost != null;
        log.info("Create gRPC server at {} on port {}", grpcHost, grpcPort);
        return  NettyServerBuilder
                .forAddress(new InetSocketAddress(grpcHost,grpcPort))
                .addService(mediaHandlerGRPCService)
                .intercept(new MediaHandlerInterceptor())
                .build();
    }

}
