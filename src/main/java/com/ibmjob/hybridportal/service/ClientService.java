package com.ibmjob.hybridportal.service;

import com.ibmjob.hybridportal.domain.ClientProfile;
import com.ibmjob.hybridportal.repository.ClientProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientProfileRepository clientProfileRepository;

    public ClientService(ClientProfileRepository clientProfileRepository) {
        this.clientProfileRepository = clientProfileRepository;
    }

    public List<ClientProfile> findAll() {
        return clientProfileRepository.findAll();
    }

    public List<ClientProfile> search(String query) {
        if (query == null || query.isBlank()) {
            return sortedByReadiness();
        }
        return clientProfileRepository.findByNameContainingIgnoreCaseOrIndustryContainingIgnoreCase(query, query);
    }

    public ClientProfile findById(Long id) {
        return clientProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));
    }

    public ClientProfile save(ClientProfile clientProfile) {
        return clientProfileRepository.save(clientProfile);
    }

    public List<ClientProfile> sortedByReadiness() {
        return clientProfileRepository.findAll().stream()
                .sorted(Comparator.comparingInt(ClientProfile::getAiReadinessScore).reversed())
                .toList();
    }

    public Map<String, List<ClientProfile>> groupByIndustry() {
        return clientProfileRepository.findAll().stream()
                .collect(Collectors.groupingBy(ClientProfile::getIndustry, TreeMap::new, Collectors.toList()));
    }
}
