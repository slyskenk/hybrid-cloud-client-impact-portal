package com.ibmjob.hybridportal.service;

import com.ibmjob.hybridportal.domain.ClientProfile;
import com.ibmjob.hybridportal.domain.ProjectStatus;
import com.ibmjob.hybridportal.dto.AnalyticsSummary;
import com.ibmjob.hybridportal.repository.ClientProfileRepository;
import com.ibmjob.hybridportal.repository.ConsultingProjectRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final ClientProfileRepository clientProfileRepository;
    private final ConsultingProjectRepository consultingProjectRepository;

    public AnalyticsService(ClientProfileRepository clientProfileRepository, ConsultingProjectRepository consultingProjectRepository) {
        this.clientProfileRepository = clientProfileRepository;
        this.consultingProjectRepository = consultingProjectRepository;
    }

    public AnalyticsSummary summary() {
        var clients = clientProfileRepository.findAll();
        double averageReadiness = clients.stream()
                .mapToInt(ClientProfile::getAiReadinessScore)
                .average()
                .orElse(0);
        Map<String, Long> clientsByIndustry = clients.stream()
                .collect(Collectors.groupingBy(ClientProfile::getIndustry, Collectors.counting()));

        long blockedProjects = consultingProjectRepository.countByStatus(ProjectStatus.BLOCKED);
        long activeProjects = consultingProjectRepository.count() - consultingProjectRepository.countByStatus(ProjectStatus.COMPLETED);

        return new AnalyticsSummary(clients.size(), activeProjects, blockedProjects, averageReadiness, clientsByIndustry);
    }
}
