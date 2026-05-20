package com.ibmjob.hybridportal.dto;

import java.util.Map;

public record AnalyticsSummary(
        long totalClients,
        long activeProjects,
        long blockedProjects,
        double averageReadinessScore,
        Map<String, Long> clientsByIndustry
) {
}
