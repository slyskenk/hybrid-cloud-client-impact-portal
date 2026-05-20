package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.ProjectStatus;

public class ProjectStatusUpdate {

    private ProjectStatus status;

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}
