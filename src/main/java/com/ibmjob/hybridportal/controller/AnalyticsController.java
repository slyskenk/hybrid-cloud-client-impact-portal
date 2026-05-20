package com.ibmjob.hybridportal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibmjob.hybridportal.domain.RiskLevel;
import com.ibmjob.hybridportal.dto.AnalyticsSummary;
import com.ibmjob.hybridportal.dto.CloudAssessmentRequest;
import com.ibmjob.hybridportal.dto.CloudRecommendation;
import com.ibmjob.hybridportal.service.AnalyticsService;
import com.ibmjob.hybridportal.service.CloudAnalyzerService;
import com.ibmjob.hybridportal.service.ImportExportService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final CloudAnalyzerService cloudAnalyzerService;
    private final ImportExportService importExportService;

    public AnalyticsController(AnalyticsService analyticsService, CloudAnalyzerService cloudAnalyzerService, ImportExportService importExportService) {
        this.analyticsService = analyticsService;
        this.cloudAnalyzerService = cloudAnalyzerService;
        this.importExportService = importExportService;
    }

    @GetMapping
    public AnalyticsSummary summary() {
        return analyticsService.summary();
    }

    @PostMapping(value = "/recommendations", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CloudRecommendation recommendFromJson(@RequestBody CloudAssessmentRequest request) {
        return cloudAnalyzerService.analyze(request);
    }

    @PostMapping(value = "/recommendations/xml", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public CloudRecommendation recommendFromXml(@RequestBody String payload) throws JsonProcessingException {
        return cloudAnalyzerService.analyze(importExportService.readXml(payload));
    }

    @GetMapping(value = "/recommendations/export/sample", produces = MediaType.APPLICATION_JSON_VALUE)
    public String exportSampleRecommendation() throws JsonProcessingException {
        CloudAssessmentRequest request = new CloudAssessmentRequest();
        request.setClientName("Horizon Financial");
        request.setWorkloads(List.of("payments", "fraud analytics", "customer portal"));
        request.setLegacySystemCount(5);
        request.setComplianceRequired(true);
        request.setCurrentCloudUsagePercent(35);
        request.setAutomationMaturity(4);
        request.setIntegrationComplexity(6);
        request.setDataSensitivity(RiskLevel.HIGH);
        return importExportService.exportRecommendation(cloudAnalyzerService.analyze(request));
    }
}
