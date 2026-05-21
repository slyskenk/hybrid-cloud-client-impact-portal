package com.ibmjob.hybridportal.dto;

import com.ibmjob.hybridportal.domain.Milestone;

import java.time.LocalDate;

public record MilestoneResponse(
        Long id,
        String title,
        LocalDate targetDate,
        boolean complete
) {

    public static MilestoneResponse from(Milestone milestone) {
        return new MilestoneResponse(
                milestone.getId(),
                milestone.getTitle(),
                milestone.getTargetDate(),
                milestone.isComplete()
        );
    }
}
