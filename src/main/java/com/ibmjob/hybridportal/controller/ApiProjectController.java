package com.ibmjob.hybridportal.controller;

import com.ibmjob.hybridportal.domain.ConsultingProject;
import com.ibmjob.hybridportal.dto.MilestoneRequest;
import com.ibmjob.hybridportal.dto.MilestoneStatusUpdate;
import com.ibmjob.hybridportal.dto.ProjectRequest;
import com.ibmjob.hybridportal.dto.ProjectResponse;
import com.ibmjob.hybridportal.dto.ProjectStatusUpdate;
import com.ibmjob.hybridportal.service.ProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ApiProjectController {

    private static final Logger log = LoggerFactory.getLogger(ApiProjectController.class);

    private final ProjectService projectService;

    public ApiProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectResponse> list() {
        return projectService.findAll().stream()
                .map(ProjectResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@Valid @RequestBody ProjectRequest request) {
        log.info("Creating project name={} clientId={}", request.getName(), request.getClientId());
        return ProjectResponse.from(projectService.create(request));
    }

    @PatchMapping("/{id}/status")
    public ProjectResponse updateStatus(@PathVariable Long id, @Valid @RequestBody ProjectStatusUpdate update) {
        log.info("Updating project id={} status={}", id, update.getStatus());
        return ProjectResponse.from(projectService.updateStatus(id, update.getStatus()));
    }

    @PostMapping("/{id}/milestones")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse addMilestone(@PathVariable Long id, @Valid @RequestBody MilestoneRequest request) {
        log.info("Adding milestone projectId={} title={}", id, request.getTitle());
        return ProjectResponse.from(projectService.addMilestone(id, request));
    }

    @PatchMapping("/{id}/milestones/{milestoneId}")
    public ProjectResponse updateMilestone(@PathVariable Long id, @PathVariable Long milestoneId, @Valid @RequestBody MilestoneStatusUpdate update) {
        log.info("Updating milestone projectId={} milestoneId={} complete={}", id, milestoneId, update.getComplete());
        return ProjectResponse.from(projectService.updateMilestoneCompletion(id, milestoneId, update.getComplete()));
    }
}
