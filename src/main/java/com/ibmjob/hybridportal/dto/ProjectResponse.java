package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.ConsultingProject;
import com.ibmjob.hybridportal.domain.MigrationPath;
import com.ibmjob.hybridportal.domain.ProjectStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

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
        String clientName,
        List<MilestoneResponse> milestones,
        int milestoneCompletionPercent
) {

    public static ProjectResponse from(ConsultingProject project) {
        List<MilestoneResponse> milestones = project.getMilestones().stream()
                .sorted(Comparator.comparing(milestone -> milestone.getTargetDate() == null ? LocalDate.MAX : milestone.getTargetDate()))
                .map(MilestoneResponse::from)
                .toList();
        int completionPercent = milestones.isEmpty()
                ? 0
                : (int) Math.round(milestones.stream().filter(MilestoneResponse::complete).count() * 100.0 / milestones.size());

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
                project.getClient().getName(),
                milestones,
                completionPercent
        );
    }
}
