package com.visraj.paymentservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced  // Required for resolving service names like http://ORDER_SERVICE
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
