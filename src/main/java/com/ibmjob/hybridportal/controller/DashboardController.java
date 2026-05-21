package com.ibmjob.hybridportal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibmjob.hybridportal.domain.MigrationPath;
import com.ibmjob.hybridportal.domain.ProjectStatus;
import com.ibmjob.hybridportal.domain.RiskLevel;
import com.ibmjob.hybridportal.dto.AnalyzerForm;
import com.ibmjob.hybridportal.dto.ClientRequest;
import com.ibmjob.hybridportal.dto.CloudAssessmentRequest;
import com.ibmjob.hybridportal.dto.CloudRecommendation;
import com.ibmjob.hybridportal.dto.MilestoneRequest;
import com.ibmjob.hybridportal.dto.ProjectRequest;
import com.ibmjob.hybridportal.dto.ProjectStatusUpdate;
import com.ibmjob.hybridportal.service.AnalyticsService;
import com.ibmjob.hybridportal.service.ClientService;
import com.ibmjob.hybridportal.service.CloudAnalyzerService;
import com.ibmjob.hybridportal.service.ImportExportService;
import com.ibmjob.hybridportal.service.MockCloudService;
import com.ibmjob.hybridportal.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final ClientService clientService;
    private final ProjectService projectService;
    private final AnalyticsService analyticsService;
    private final MockCloudService mockCloudService;
    private final CloudAnalyzerService cloudAnalyzerService;
    private final ImportExportService importExportService;

    public DashboardController(ClientService clientService, ProjectService projectService, AnalyticsService analyticsService, MockCloudService mockCloudService, CloudAnalyzerService cloudAnalyzerService, ImportExportService importExportService) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.analyticsService = analyticsService;
        this.mockCloudService = mockCloudService;
        this.cloudAnalyzerService = cloudAnalyzerService;
        this.importExportService = importExportService;
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
        addClientModel(model);
        return "clients";
    }

    @PostMapping("/clients")
    public String createClient(@Valid @ModelAttribute("clientForm") ClientRequest clientRequest, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addClientModel(model);
            model.addAttribute("formError", "Review the client fields and try again.");
            return "clients";
        }
        clientService.create(clientRequest);
        redirectAttributes.addFlashAttribute("success", "Client profile created.");
        return "redirect:/clients";
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        addProjectModel(model);
        return "projects";
    }

    @PostMapping("/projects")
    public String createProject(@Valid @ModelAttribute("projectForm") ProjectRequest projectRequest, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addProjectModel(model);
            model.addAttribute("formError", "Review the project fields and try again.");
            return "projects";
        }
        projectService.create(projectRequest);
        redirectAttributes.addFlashAttribute("success", "Project created.");
        return "redirect:/projects";
    }

    @PostMapping("/projects/{id}/status")
    public String updateProjectStatus(@PathVariable Long id, @Valid @ModelAttribute ProjectStatusUpdate statusUpdate, RedirectAttributes redirectAttributes) {
        projectService.updateStatus(id, statusUpdate.getStatus());
        redirectAttributes.addFlashAttribute("success", "Project status updated.");
        return "redirect:/projects";
    }

    @PostMapping("/projects/{projectId}/milestones")
    public String addMilestone(@PathVariable Long projectId, @Valid @ModelAttribute MilestoneRequest milestoneRequest, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addProjectModel(model);
            model.addAttribute("formError", "Add a milestone title and target date.");
            return "projects";
        }
        projectService.addMilestone(projectId, milestoneRequest);
        redirectAttributes.addFlashAttribute("success", "Milestone added.");
        return "redirect:/projects";
    }

    @PostMapping("/projects/{projectId}/milestones/{milestoneId}/complete")
    public String updateMilestoneCompletion(@PathVariable Long projectId, @PathVariable Long milestoneId, @RequestParam boolean complete, RedirectAttributes redirectAttributes) {
        projectService.updateMilestoneCompletion(projectId, milestoneId, complete);
        redirectAttributes.addFlashAttribute("success", "Milestone updated.");
        return "redirect:/projects";
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        addAnalyticsModel(model);
        return "analytics";
    }

    @GetMapping("/analytics/sample")
    @ResponseBody
    public CloudRecommendation sampleAnalysis() throws JsonProcessingException {
        CloudAssessmentRequest request = importExportService.readJson(new AnalyzerForm().getPayload());
        return cloudAnalyzerService.analyze(request);
    }

    @PostMapping("/analytics")
    public String analyze(@ModelAttribute("analyzerForm") AnalyzerForm analyzerForm, Model model) {
        addAnalyticsModel(model);
        try {
            CloudAssessmentRequest request = "XML".equalsIgnoreCase(analyzerForm.getFormat())
                    ? importExportService.readXml(analyzerForm.getPayload())
                    : importExportService.readJson(analyzerForm.getPayload());
            CloudRecommendation recommendation = cloudAnalyzerService.analyze(request);
            model.addAttribute("recommendation", recommendation);
            model.addAttribute("recommendationJson", importExportService.exportRecommendation(recommendation));
        } catch (JsonProcessingException exception) {
            model.addAttribute("analysisError", "The assessment could not be parsed. Check the selected format and payload.");
        }
        model.addAttribute("analyzerForm", analyzerForm);
        return "analytics";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/favicon.ico")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void favicon() {
    }

    private void addClientModel(Model model) {
        model.addAttribute("clients", clientService.sortedByReadiness());
        model.addAttribute("industries", clientService.groupByIndustry());
        model.addAttribute("riskLevels", RiskLevel.values());
        if (!model.containsAttribute("clientForm")) {
            model.addAttribute("clientForm", new ClientRequest());
        }
    }

    private void addProjectModel(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("clients", clientService.sortedByReadiness());
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("migrationPaths", MigrationPath.values());
        if (!model.containsAttribute("projectForm")) {
            ProjectRequest projectRequest = new ProjectRequest();
            projectRequest.setDeadline(LocalDate.now().plusMonths(2));
            model.addAttribute("projectForm", projectRequest);
        }
    }

    private void addAnalyticsModel(Model model) {
        model.addAttribute("summary", analyticsService.summary());
        model.addAttribute("formats", new String[]{"JSON", "XML"});
        if (!model.containsAttribute("analyzerForm")) {
            model.addAttribute("analyzerForm", new AnalyzerForm());
        }
    }
}
