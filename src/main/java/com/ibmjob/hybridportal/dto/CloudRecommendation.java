package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.MigrationPath;
import com.ibmjob.hybridportal.domain.RiskLevel;

import java.util.List;

public record CloudRecommendation(
        String clientName,
        int readinessScore,
        RiskLevel riskLevel,
        MigrationPath migrationPath,
        int estimatedSprintCount,
        List<String> recommendations
) {
}
