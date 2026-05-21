package com.ibmjob.hybridportal.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ConsultingProject extends PortfolioRecord {

    @NotBlank
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PLANNED;

    private LocalDate deadline;

    @Min(1)
    @Max(5)
    private int priority = 3;

    private String assignedConsultant;

    @Enumerated(EnumType.STRING)
    private MigrationPath migrationPath = MigrationPath.HYBRID_CLOUD;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private ClientProfile client;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("targetDate ASC")
    private List<Milestone> milestones = new ArrayList<>();

    public ConsultingProject() {
    }

    public ConsultingProject(String name, String description, ProjectStatus status, LocalDate deadline, int priority, String assignedConsultant, MigrationPath migrationPath) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.priority = priority;
        this.assignedConsultant = assignedConsultant;
        this.migrationPath = migrationPath;
    }

    public void addMilestone(Milestone milestone) {
        milestones.add(milestone);
        milestone.setProject(this);
    }

    public boolean isBlocked() {
        return ProjectStatus.BLOCKED.equals(status);
    }

    public long getCompletedMilestoneCount() {
        return milestones.stream()
                .filter(Milestone::isComplete)
                .count();
    }

    public int getMilestoneCompletionPercent() {
        if (milestones.isEmpty()) {
            return 0;
        }
        return (int) Math.round(getCompletedMilestoneCount() * 100.0 / milestones.size());
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

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
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

    public ClientProfile getClient() {
        return client;
    }

    public void setClient(ClientProfile client) {
        this.client = client;
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }
}
