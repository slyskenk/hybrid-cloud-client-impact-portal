package com.ibmjob.hybridportal.dto;

import jakarta.validation.constraints.NotNull;

public class MilestoneStatusUpdate {

    @NotNull
    private Boolean complete;

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
}
