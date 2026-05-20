package com.ibmjob.hybridportal.dto;

public class AnalyzerForm {

    private String format = "JSON";

    private String payload = """
            {
              "clientName": "Horizon Financial",
              "workloads": ["payments", "fraud analytics", "customer portal"],
              "legacySystemCount": 5,
              "complianceRequired": true,
              "currentCloudUsagePercent": 35,
              "automationMaturity": 4,
              "integrationComplexity": 6,
              "dataSensitivity": "HIGH"
            }
            """;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
