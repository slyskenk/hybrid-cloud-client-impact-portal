package com.ibmjob.hybridportal.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private LocalDate targetDate;

    private boolean complete;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private ConsultingProject project;

    public Milestone() {
    }

    public Milestone(String title, LocalDate targetDate, boolean complete) {
        this.title = title;
        this.targetDate = targetDate;
        this.complete = complete;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public ConsultingProject getProject() {
        return project;
    }

    public void setProject(ConsultingProject project) {
        this.project = project;
    }
}
