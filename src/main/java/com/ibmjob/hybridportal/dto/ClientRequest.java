package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.RiskLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ClientRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String industry;

    @NotBlank
    private String region;

    @Email
    @NotBlank
    private String contactEmail;

    @Min(0)
    @Max(100)
    private int aiReadinessScore = 50;

    private RiskLevel riskLevel = RiskLevel.MEDIUM;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public int getAiReadinessScore() {
        return aiReadinessScore;
    }

    public void setAiReadinessScore(int aiReadinessScore) {
        this.aiReadinessScore = aiReadinessScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }
}
