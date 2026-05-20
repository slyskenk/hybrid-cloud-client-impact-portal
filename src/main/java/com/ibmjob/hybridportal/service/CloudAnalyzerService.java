package com.ibmjob.hybridportal.service;

import com.ibmjob.hybridportal.domain.MigrationPath;
import com.ibmjob.hybridportal.domain.RiskLevel;
import com.ibmjob.hybridportal.dto.CloudAssessmentRequest;
import com.ibmjob.hybridportal.dto.CloudRecommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudAnalyzerService {

    private static final Logger log = LoggerFactory.getLogger(CloudAnalyzerService.class);

    public CloudRecommendation analyze(CloudAssessmentRequest request) {
        RiskLevel sensitivity = request.getDataSensitivity() == null ? RiskLevel.MEDIUM : request.getDataSensitivity();
        int workloadCount = request.getWorkloads() == null ? 0 : request.getWorkloads().size();

        int score = 50;
        score += Math.min(18, request.getCurrentCloudUsagePercent() / 4);
        score += Math.min(20, request.getAutomationMaturity() * 4);
        score += Math.min(10, workloadCount * 2);
        score -= Math.min(25, request.getLegacySystemCount() * 4);
        score -= Math.min(18, request.getIntegrationComplexity() * 3);
        score -= request.isComplianceRequired() ? 5 : 0;
        score -= RiskLevel.HIGH.equals(sensitivity) ? 10 : RiskLevel.MEDIUM.equals(sensitivity) ? 3 : 0;
        score = clamp(score, 0, 100);

        RiskLevel riskLevel = classifyRisk(score, sensitivity);
        MigrationPath migrationPath = chooseMigrationPath(request, score, sensitivity);
        List<String> recommendations = buildRecommendations(request, score, sensitivity, migrationPath);
        int sprintCount = estimateSprintCount(request, migrationPath);

        log.info("Generated cloud recommendation for client={} score={} path={}", request.getClientName(), score, migrationPath);
        return new CloudRecommendation(request.getClientName(), score, riskLevel, migrationPath, sprintCount, recommendations);
    }

    private RiskLevel classifyRisk(int score, RiskLevel sensitivity) {
        if (score >= 75 && !RiskLevel.HIGH.equals(sensitivity)) {
            return RiskLevel.LOW;
        }
        if (score >= 50) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.HIGH;
    }

    private MigrationPath chooseMigrationPath(CloudAssessmentRequest request, int score, RiskLevel sensitivity) {
        if (request.getLegacySystemCount() >= 6 || score < 45) {
            return MigrationPath.MODERNIZATION_FIRST;
        }
        if (request.isComplianceRequired() || RiskLevel.HIGH.equals(sensitivity)) {
            return MigrationPath.HYBRID_CLOUD;
        }
        if (request.getCurrentCloudUsagePercent() >= 60) {
            return MigrationPath.PUBLIC_CLOUD;
        }
        return MigrationPath.HYBRID_CLOUD;
    }

    private List<String> buildRecommendations(CloudAssessmentRequest request, int score, RiskLevel sensitivity, MigrationPath migrationPath) {
        Map<MigrationPath, String> pathGuidance = Map.of(
                MigrationPath.PUBLIC_CLOUD, "Expand standardized workloads into public cloud services with FinOps guardrails.",
                MigrationPath.PRIVATE_CLOUD, "Prioritize controlled private cloud services for sensitive workloads.",
                MigrationPath.HYBRID_CLOUD, "Use a hybrid landing zone with shared identity, observability, and compliance controls.",
                MigrationPath.MODERNIZATION_FIRST, "Refactor legacy applications before large-scale migration."
        );

        List<String> recommendations = new ArrayList<>();
        recommendations.add(pathGuidance.get(migrationPath));

        if (request.getLegacySystemCount() > 3) {
            recommendations.add("Create an application dependency map before sequencing migration waves.");
        }
        if (request.getAutomationMaturity() < 4) {
            recommendations.add("Increase automation with infrastructure as code and CI/CD policy checks.");
        }
        if (request.isComplianceRequired() || RiskLevel.HIGH.equals(sensitivity)) {
            recommendations.add("Add encryption, audit logging, and data residency controls to the target architecture.");
        }
        if (score < 55) {
            recommendations.add("Run a readiness sprint to reduce delivery risk before committing to aggressive timelines.");
        }
        return recommendations;
    }

    private int estimateSprintCount(CloudAssessmentRequest request, MigrationPath migrationPath) {
        int base = MigrationPath.MODERNIZATION_FIRST.equals(migrationPath) ? 6 : 4;
        int legacyLoad = (int) Math.ceil(request.getLegacySystemCount() / 2.0);
        int integrationLoad = (int) Math.ceil(request.getIntegrationComplexity() / 2.0);
        return Math.max(3, base + legacyLoad + integrationLoad);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
