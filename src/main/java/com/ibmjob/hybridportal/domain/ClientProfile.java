package com.ibmjob.hybridportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ClientProfile extends PortfolioRecord {

    @NotBlank
    private String name;

    @NotBlank
    private String industry;

    private String region;

    @Email
    private String contactEmail;

    @Min(0)
    @Max(100)
    private int aiReadinessScore;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel = RiskLevel.MEDIUM;

    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultingProject> projects = new ArrayList<>();

    public ClientProfile() {
    }

    public ClientProfile(String name, String industry, String region, String contactEmail, int aiReadinessScore, RiskLevel riskLevel) {
        this.name = name;
        this.industry = industry;
        this.region = region;
        this.contactEmail = contactEmail;
        this.aiReadinessScore = aiReadinessScore;
        this.riskLevel = riskLevel;
    }

    public void addProject(ConsultingProject project) {
        projects.add(project);
        project.setClient(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public int getAiReadinessScore() {
        return aiReadinessScore;
    }

    public void setAiReadinessScore(int aiReadinessScore) {
        this.aiReadinessScore = aiReadinessScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<ConsultingProject> getProjects() {
        return projects;
    }

    public void setProjects(List<ConsultingProject> projects) {
        this.projects = projects;
    }
}
