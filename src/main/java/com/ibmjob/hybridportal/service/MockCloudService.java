package com.ibmjob.hybridportal.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MockCloudService {

    public List<Map<String, Object>> resources() {
        return List.of(
                Map.of("provider", "IBM Cloud", "type", "Kubernetes Cluster", "region", "us-south", "monthlyCost", 1840, "health", "GREEN"),
                Map.of("provider", "Private Cloud", "type", "VMware Workload", "region", "on-prem", "monthlyCost", 980, "health", "YELLOW"),
                Map.of("provider", "IBM Cloud", "type", "Object Storage", "region", "eu-de", "monthlyCost", 420, "health", "GREEN")
        );
    }
}
