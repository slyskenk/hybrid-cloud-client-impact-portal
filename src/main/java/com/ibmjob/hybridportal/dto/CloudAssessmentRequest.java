package com.ibmjob.hybridportal.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ibmjob.hybridportal.domain.RiskLevel;

import java.util.ArrayList;
import java.util.List;

public class CloudAssessmentRequest {

    private String clientName;

    @JacksonXmlElementWrapper(localName = "workloads")
    @JacksonXmlProperty(localName = "workload")
    private List<String> workloads = new ArrayList<>();

    private int legacySystemCount;

    private boolean complianceRequired;

    private int currentCloudUsagePercent;

    private int automationMaturity;

    private int integrationComplexity;

    private RiskLevel dataSensitivity = RiskLevel.MEDIUM;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<String> getWorkloads() {
        return workloads;
    }

    public void setWorkloads(List<String> workloads) {
        this.workloads = workloads;
    }

    public int getLegacySystemCount() {
        return legacySystemCount;
    }

    public void setLegacySystemCount(int legacySystemCount) {
        this.legacySystemCount = legacySystemCount;
    }

    public boolean isComplianceRequired() {
        return complianceRequired;
    }

    public void setComplianceRequired(boolean complianceRequired) {
        this.complianceRequired = complianceRequired;
    }

    public int getCurrentCloudUsagePercent() {
        return currentCloudUsagePercent;
    }

    public void setCurrentCloudUsagePercent(int currentCloudUsagePercent) {
        this.currentCloudUsagePercent = currentCloudUsagePercent;
    }

    public int getAutomationMaturity() {
        return automationMaturity;
    }

    public void setAutomationMaturity(int automationMaturity) {
        this.automationMaturity = automationMaturity;
    }

    public int getIntegrationComplexity() {
        return integrationComplexity;
    }

    public void setIntegrationComplexity(int integrationComplexity) {
        this.integrationComplexity = integrationComplexity;
    }

    public RiskLevel getDataSensitivity() {
        return dataSensitivity;
    }

    public void setDataSensitivity(RiskLevel dataSensitivity) {
        this.dataSensitivity = dataSensitivity;
    }
}
