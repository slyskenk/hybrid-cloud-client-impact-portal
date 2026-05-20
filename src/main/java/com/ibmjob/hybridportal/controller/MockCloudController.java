package com.ibmjob.hybridportal.controller;

import com.ibmjob.hybridportal.service.MockCloudService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock-cloud")
public class MockCloudController {

    private final MockCloudService mockCloudService;

    public MockCloudController(MockCloudService mockCloudService) {
        this.mockCloudService = mockCloudService;
    }

    @GetMapping("/resources")
    public List<Map<String, Object>> resources() {
        return mockCloudService.resources();
    }
}
