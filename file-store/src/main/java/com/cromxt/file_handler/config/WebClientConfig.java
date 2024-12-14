package com.cromxt.file_handler.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient gatewayWebClient(WebClient.Builder webClientBuilder, Environment environment) {
        String gatewayURL = environment.getProperty("FILE_STORE.GATEWAY_URL", String.class, "http://localhost:9090");
        return webClientBuilder.baseUrl(gatewayURL).build();
    }
}
