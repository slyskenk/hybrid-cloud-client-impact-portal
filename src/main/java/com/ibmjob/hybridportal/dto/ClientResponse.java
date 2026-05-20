package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.ClientProfile;
import com.ibmjob.hybridportal.domain.RiskLevel;

import java.time.Instant;

public record ClientResponse(
        Long id,
        Instant createdAt,
        String name,
        String industry,
        String region,
        String contactEmail,
        int aiReadinessScore,
        RiskLevel riskLevel
) {

    public static ClientResponse from(ClientProfile client) {
        return new ClientResponse(
                client.getId(),
                client.getCreatedAt(),
                client.getName(),
                client.getIndustry(),
                client.getRegion(),
                client.getContactEmail(),
                client.getAiReadinessScore(),
                client.getRiskLevel()
        );
    }
}
