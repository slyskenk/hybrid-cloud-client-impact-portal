package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.MigrationPath;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ProjectRequest {

    @NotNull
    private Long clientId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate deadline;

    @Min(1)
    @Max(5)
    private int priority = 3;

    @NotBlank
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
