package com.ibmjob.hybridportal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ibmjob.hybridportal.dto.CloudAssessmentRequest;
import com.ibmjob.hybridportal.dto.CloudRecommendation;
import org.springframework.stereotype.Service;

@Service
public class ImportExportService {

    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    public ImportExportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.xmlMapper = new XmlMapper();
    }

    public CloudAssessmentRequest readJson(String payload) throws JsonProcessingException {
        return objectMapper.readValue(payload, CloudAssessmentRequest.class);
    }

    public CloudAssessmentRequest readXml(String payload) throws JsonProcessingException {
        return xmlMapper.readValue(payload, CloudAssessmentRequest.class);
    }

    public String exportRecommendation(CloudRecommendation recommendation) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(recommendation);
    }
}
