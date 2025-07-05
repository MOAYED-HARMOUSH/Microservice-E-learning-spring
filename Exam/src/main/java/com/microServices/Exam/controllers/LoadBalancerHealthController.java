package com.microServices.Exam.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load Balancer Health Check Controller
 * Provides endpoints for monitoring load balancer status
 */
@RestController
@RequestMapping("/api/loadbalancer")
public class LoadBalancerHealthController {

    private final DiscoveryClient discoveryClient;
    private final LoadBalancerClientFactory loadBalancerClientFactory;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.port}")
    private String serverPort;

    public LoadBalancerHealthController(DiscoveryClient discoveryClient, 
                                      LoadBalancerClientFactory loadBalancerClientFactory) {
        this.discoveryClient = discoveryClient;
        this.loadBalancerClientFactory = loadBalancerClientFactory;
    }

    /**
     * Get current service instance information
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("service", serviceName);
        healthInfo.put("port", serverPort);
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(healthInfo);
    }

    /**
     * Get all registered instances of this service
     */
    @GetMapping("/instances")
    public ResponseEntity<Map<String, Object>> getInstances() {
        Map<String, Object> instancesInfo = new HashMap<>();
        
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        instancesInfo.put("service", serviceName);
        instancesInfo.put("totalInstances", instances.size());
        instancesInfo.put("instances", instances);
        
        return ResponseEntity.ok(instancesInfo);
    }

    /**
     * Get load balancer statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getLoadBalancerStats() {
        Map<String, Object> stats = new HashMap<>();
        
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        
        stats.put("service", serviceName);
        stats.put("activeInstances", instances.size());
        stats.put("currentInstance", serviceName + ":" + serverPort);
        stats.put("loadBalancerType", "Round Robin");
        stats.put("cacheEnabled", true);
        stats.put("healthCheckEnabled", true);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * Get all services in the registry
     */
    @GetMapping("/services")
    public ResponseEntity<Map<String, Object>> getAllServices() {
        Map<String, Object> servicesInfo = new HashMap<>();
        
        List<String> services = discoveryClient.getServices();
        servicesInfo.put("totalServices", services.size());
        servicesInfo.put("services", services);
        
        return ResponseEntity.ok(servicesInfo);
    }
} 