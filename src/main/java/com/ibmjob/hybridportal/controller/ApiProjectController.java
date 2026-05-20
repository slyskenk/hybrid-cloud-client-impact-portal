package com.ibmjob.hybridportal.controller;

import com.ibmjob.hybridportal.domain.ConsultingProject;
import com.ibmjob.hybridportal.dto.ProjectRequest;
import com.ibmjob.hybridportal.dto.ProjectStatusUpdate;
import com.ibmjob.hybridportal.service.ProjectService;
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
    public List<ConsultingProject> list() {
        return projectService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultingProject create(@RequestBody ProjectRequest request) {
        log.info("Creating project name={} clientId={}", request.getName(), request.getClientId());
        return projectService.create(request);
    }

    @PatchMapping("/{id}/status")
    public ConsultingProject updateStatus(@PathVariable Long id, @RequestBody ProjectStatusUpdate update) {
        log.info("Updating project id={} status={}", id, update.getStatus());
        return projectService.updateStatus(id, update.getStatus());
    }
}
