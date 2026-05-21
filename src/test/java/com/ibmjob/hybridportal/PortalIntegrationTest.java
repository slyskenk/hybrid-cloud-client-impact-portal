package com.ibmjob.hybridportal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PortalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void dashboardRedirectsAnonymousUsersToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void faviconRequestIsHandledWithoutAnApplicationError() throws Exception {
        mockMvc.perform(get("/favicon.ico"))
                .andExpect(status().isNoContent());
    }

    @Test
    void missingStaticResourcesReturnNotFound() throws Exception {
        mockMvc.perform(get("/css/missing.css"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    @WithMockUser(roles = "CONSULTANT")
    void clientPageLoadsForConsultants() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Horizon Financial")));
    }

    @Test
    @WithMockUser(roles = "CONSULTANT")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void projectPageShowsAndCreatesMilestones() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Architecture review")));

        mockMvc.perform(post("/projects/1/milestones")
                        .with(csrf())
                        .param("title", "Executive checkpoint")
                        .param("targetDate", "2026-07-18"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Executive checkpoint")));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void clientRoleCannotAccessConsultantWorkflows() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/projects"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/analytics"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CONSULTANT")
    void dashboardSampleAnalysisWorksForFormLoginUsers() throws Exception {
        mockMvc.perform(get("/analytics/sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Horizon Financial"))
                .andExpect(jsonPath("$.riskLevel").value("HIGH"));
    }

    @Test
    @WithMockUser(roles = "CONSULTANT")
    void analyticsWebFormDisplaysClientNameAndRecommendation() throws Exception {
        String payload = """
                {
                  "clientName": "Apex Utilities",
                  "workloads": ["billing", "asset telemetry", "customer service"],
                  "legacySystemCount": 5,
                  "complianceRequired": true,
                  "currentCloudUsagePercent": 35,
                  "automationMaturity": 3,
                  "integrationComplexity": 6,
                  "dataSensitivity": "HIGH"
                }
                """;

        mockMvc.perform(post("/analytics")
                        .with(csrf())
                        .param("format", "JSON")
                        .param("payload", payload))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Apex Utilities")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("MODERNIZATION_FIRST")));
    }

    @Test
    void analyticsApiAcceptsBasicAuth() throws Exception {
        mockMvc.perform(get("/api/analytics").with(httpBasic("consultant", "consult123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalClients").value(3))
                .andExpect(jsonPath("$.blockedProjects").value(1));
    }

    @Test
    void analyzerApiReturnsRecommendation() throws Exception {
        String payload = """
                {
                  "clientName": "Horizon Financial",
                  "workloads": ["payments", "fraud analytics", "customer portal"],
                  "legacySystemCount": 5,
                  "complianceRequired": true,
                  "currentCloudUsagePercent": 35,
                  "automationMaturity": 4,
                  "integrationComplexity": 6,
                  "dataSensitivity": "HIGH"
                }
                """;

        mockMvc.perform(post("/api/analytics/recommendations")
                        .with(httpBasic("consultant", "consult123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("HIGH"))
                .andExpect(jsonPath("$.migrationPath").value("MODERNIZATION_FIRST"));
    }

    @Test
    void malformedAnalyzerApiPayloadReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/analytics/recommendations")
                        .with(httpBasic("consultant", "consult123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not valid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request payload could not be parsed"));
    }

    @Test
    void clientApiCreatesClientWithRequestDto() throws Exception {
        String payload = """
                {
                  "name": "Atlas Energy",
                  "industry": "Energy",
                  "region": "Africa",
                  "contactEmail": "cloud@atlas.example",
                  "aiReadinessScore": 71,
                  "riskLevel": "MEDIUM"
                }
                """;

        mockMvc.perform(post("/api/clients")
                        .with(httpBasic("consultant", "consult123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Atlas Energy"))
                .andExpect(jsonPath("$.riskLevel").value("MEDIUM"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void projectApiReturnsAndUpdatesMilestones() throws Exception {
        mockMvc.perform(get("/api/projects").with(httpBasic("consultant", "consult123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].milestones[0].title").value("Architecture review"))
                .andExpect(jsonPath("$[0].milestoneCompletionPercent").value(50));

        String newMilestone = """
                {
                  "title": "Go-live readiness",
                  "targetDate": "2026-08-01",
                  "complete": false
                }
                """;

        mockMvc.perform(post("/api/projects/1/milestones")
                        .with(httpBasic("consultant", "consult123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMilestone))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.milestones[2].title").value("Go-live readiness"))
                .andExpect(jsonPath("$.milestoneCompletionPercent").value(33));

        mockMvc.perform(patch("/api/projects/1/milestones/2")
                        .with(httpBasic("consultant", "consult123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"complete\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestones[1].complete").value(true));
    }
}
