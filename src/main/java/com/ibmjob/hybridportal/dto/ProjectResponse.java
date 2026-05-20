package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.ConsultingProject;
import com.ibmjob.hybridportal.domain.MigrationPath;
import com.ibmjob.hybridportal.domain.ProjectStatus;

import java.time.LocalDate;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        ProjectStatus status,
        LocalDate deadline,
        int priority,
        String assignedConsultant,
        MigrationPath migrationPath,
        Long clientId,
        String clientName
) {

    public static ProjectResponse from(ConsultingProject project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getDeadline(),
                project.getPriority(),
                project.getAssignedConsultant(),
                project.getMigrationPath(),
                project.getClient().getId(),
                project.getClient().getName()
        );
    }
}
