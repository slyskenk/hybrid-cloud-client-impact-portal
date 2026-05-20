package com.ibmjob.hybridportal.controller;

import com.ibmjob.hybridportal.service.AnalyticsService;
import com.ibmjob.hybridportal.service.ClientService;
import com.ibmjob.hybridportal.service.MockCloudService;
import com.ibmjob.hybridportal.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ClientService clientService;
    private final ProjectService projectService;
    private final AnalyticsService analyticsService;
    private final MockCloudService mockCloudService;

    public DashboardController(ClientService clientService, ProjectService projectService, AnalyticsService analyticsService, MockCloudService mockCloudService) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.analyticsService = analyticsService;
        this.mockCloudService = mockCloudService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("summary", analyticsService.summary());
        model.addAttribute("clients", clientService.sortedByReadiness());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("resources", mockCloudService.resources());
        return "dashboard";
    }

    @GetMapping("/clients")
    public String clients(Model model) {
        model.addAttribute("clients", clientService.sortedByReadiness());
        model.addAttribute("industries", clientService.groupByIndustry());
        return "clients";
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "projects";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
