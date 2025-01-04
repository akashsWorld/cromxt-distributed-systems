package com.cromxt.bucket.config;


import com.cromxt.bucket.interceptors.MediaHandlerInterceptor;
import com.cromxt.bucket.service.impl.MediaHandlerGRPCServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Configuration
public class GrpcServerConfig {

    @Bean
    public Server mediaHandlerGrpcServer(MediaHandlerGRPCServiceImpl mediaHandlerGRPCService,
                                         Environment environment) throws IOException {
        Integer grpcPort = environment.getProperty("BUCKET_CONFIG_GRPC_SERVICE_PORT", Integer.class);
        assert grpcPort != null;
        return ServerBuilder.forPort(grpcPort)
                .addService(mediaHandlerGRPCService)
                .intercept(new MediaHandlerInterceptor())
                .build();
    }

}
