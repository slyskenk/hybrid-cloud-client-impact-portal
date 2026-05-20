package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.ProjectStatus;
import jakarta.validation.constraints.NotNull;

public class ProjectStatusUpdate {

    @NotNull
    private ProjectStatus status;

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}
