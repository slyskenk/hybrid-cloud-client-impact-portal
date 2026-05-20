package com.ibmjob.hybridportal.service;

import com.ibmjob.hybridportal.domain.MigrationPath;
import com.ibmjob.hybridportal.domain.RiskLevel;
import com.ibmjob.hybridportal.dto.CloudAssessmentRequest;
import com.ibmjob.hybridportal.dto.CloudRecommendation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CloudAnalyzerServiceTest {

    private final CloudAnalyzerService service = new CloudAnalyzerService();

    @Test
    void recommendsModernizationFirstForLegacyHeavyClient() {
        CloudAssessmentRequest request = new CloudAssessmentRequest();
        request.setClientName("Legacy Bank");
        request.setWorkloads(List.of("core banking", "payments"));
        request.setLegacySystemCount(8);
        request.setComplianceRequired(true);
        request.setCurrentCloudUsagePercent(20);
        request.setAutomationMaturity(2);
        request.setIntegrationComplexity(7);
        request.setDataSensitivity(RiskLevel.HIGH);

        CloudRecommendation recommendation = service.analyze(request);

        assertThat(recommendation.migrationPath()).isEqualTo(MigrationPath.MODERNIZATION_FIRST);
        assertThat(recommendation.riskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(recommendation.recommendations()).isNotEmpty();
    }

    @Test
    void recommendsPublicCloudForMatureLowRiskClient() {
        CloudAssessmentRequest request = new CloudAssessmentRequest();
        request.setClientName("Digital Retailer");
        request.setWorkloads(List.of("web", "recommendations", "catalog"));
        request.setLegacySystemCount(1);
        request.setComplianceRequired(false);
        request.setCurrentCloudUsagePercent(80);
        request.setAutomationMaturity(5);
        request.setIntegrationComplexity(2);
        request.setDataSensitivity(RiskLevel.LOW);

        CloudRecommendation recommendation = service.analyze(request);

        assertThat(recommendation.migrationPath()).isEqualTo(MigrationPath.PUBLIC_CLOUD);
        assertThat(recommendation.readinessScore()).isGreaterThanOrEqualTo(75);
    }
}
