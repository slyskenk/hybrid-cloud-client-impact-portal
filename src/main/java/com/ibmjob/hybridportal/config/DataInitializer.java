package com.ibmjob.hybridportal.config;

import com.ibmjob.hybridportal.domain.ClientProfile;
import com.ibmjob.hybridportal.domain.ConsultingProject;
import com.ibmjob.hybridportal.domain.MigrationPath;
import com.ibmjob.hybridportal.domain.Milestone;
import com.ibmjob.hybridportal.domain.ProjectStatus;
import com.ibmjob.hybridportal.domain.RiskLevel;
import com.ibmjob.hybridportal.domain.Role;
import com.ibmjob.hybridportal.domain.UserAccount;
import com.ibmjob.hybridportal.repository.ClientProfileRepository;
import com.ibmjob.hybridportal.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(ClientProfileRepository clientRepository, UserAccountRepository userAccountRepository) {
        return args -> {
            if (clientRepository.count() > 0) {
                return;
            }

            ClientProfile horizon = new ClientProfile("Horizon Financial", "Banking", "North America", "sponsor@horizon.example", 64, RiskLevel.HIGH);
            ConsultingProject landingZone = new ConsultingProject("Hybrid Cloud Landing Zone", "Design shared identity, network, and compliance controls for regulated workloads.", ProjectStatus.IN_PROGRESS, LocalDate.of(2026, 8, 15), 1, "Avery Stone", MigrationPath.HYBRID_CLOUD);
            landingZone.addMilestone(new Milestone("Architecture review", LocalDate.of(2026, 6, 14), true));
            landingZone.addMilestone(new Milestone("Security baseline", LocalDate.of(2026, 7, 3), false));
            horizon.addProject(landingZone);

            ClientProfile nova = new ClientProfile("Nova Retail Group", "Retail", "Europe", "transformation@nova.example", 78, RiskLevel.MEDIUM);
            ConsultingProject aiOps = new ConsultingProject("AIOps Enablement", "Connect observability signals and incident workflows across cloud platforms.", ProjectStatus.PLANNED, LocalDate.of(2026, 9, 20), 2, "Morgan Lee", MigrationPath.PUBLIC_CLOUD);
            aiOps.addMilestone(new Milestone("Telemetry inventory", LocalDate.of(2026, 6, 25), false));
            nova.addProject(aiOps);

            ClientProfile summit = new ClientProfile("Summit Health", "Healthcare", "Africa", "cio@summit.example", 49, RiskLevel.HIGH);
            ConsultingProject modernization = new ConsultingProject("Clinical Platform Modernization", "Prioritize legacy dependencies and establish an incremental migration roadmap.", ProjectStatus.BLOCKED, LocalDate.of(2026, 10, 5), 1, "Jordan Patel", MigrationPath.MODERNIZATION_FIRST);
            modernization.addMilestone(new Milestone("Dependency mapping", LocalDate.of(2026, 6, 12), false));
            summit.addProject(modernization);

            clientRepository.save(horizon);
            clientRepository.save(nova);
            clientRepository.save(summit);

            userAccountRepository.save(new UserAccount("admin", "Admin User", "admin@example.com", Role.ADMIN));
            userAccountRepository.save(new UserAccount("consultant", "Consultant User", "consultant@example.com", Role.CONSULTANT));
            userAccountRepository.save(new UserAccount("client", "Client User", "client@example.com", Role.CLIENT));
        };
    }
}
