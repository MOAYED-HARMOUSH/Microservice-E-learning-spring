package com.microServices.Exam.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate Configuration with Load Balancing Support
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Load Balanced RestTemplate for service-to-service communication
     * This will automatically use the configured load balancer
     */
    @Bean
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    /**
     * Regular RestTemplate for external API calls (without load balancing)
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 