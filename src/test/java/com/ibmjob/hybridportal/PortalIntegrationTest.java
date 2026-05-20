package com.ibmjob.hybridportal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @WithMockUser(roles = "CONSULTANT")
    void clientPageLoadsForConsultants() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Horizon Financial")));
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
}
