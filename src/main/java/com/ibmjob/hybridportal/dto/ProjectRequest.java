package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.MigrationPath;

import java.time.LocalDate;

public class ProjectRequest {

    private Long clientId;
    private String name;
    private String description;
    private LocalDate deadline;
    private int priority = 3;
    private String assignedConsultant;
    private MigrationPath migrationPath = MigrationPath.HYBRID_CLOUD;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAssignedConsultant() {
        return assignedConsultant;
    }

    public void setAssignedConsultant(String assignedConsultant) {
        this.assignedConsultant = assignedConsultant;
    }

    public MigrationPath getMigrationPath() {
        return migrationPath;
    }

    public void setMigrationPath(MigrationPath migrationPath) {
        this.migrationPath = migrationPath;
    }
}
