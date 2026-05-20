package com.ibmjob.hybridportal.service;

import com.ibmjob.hybridportal.domain.ClientProfile;
import com.ibmjob.hybridportal.domain.ConsultingProject;
import com.ibmjob.hybridportal.domain.ProjectStatus;
import com.ibmjob.hybridportal.dto.ProjectRequest;
import com.ibmjob.hybridportal.repository.ClientProfileRepository;
import com.ibmjob.hybridportal.repository.ConsultingProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ConsultingProjectRepository consultingProjectRepository;
    private final ClientProfileRepository clientProfileRepository;

    public ProjectService(ConsultingProjectRepository consultingProjectRepository, ClientProfileRepository clientProfileRepository) {
        this.consultingProjectRepository = consultingProjectRepository;
        this.clientProfileRepository = clientProfileRepository;
    }

    public List<ConsultingProject> findAll() {
        return consultingProjectRepository.findAllByOrderByDeadlineAsc();
    }

    @Transactional
    public ConsultingProject create(ProjectRequest request) {
        ClientProfile client = clientProfileRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + request.getClientId()));

        ConsultingProject project = new ConsultingProject();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setDeadline(request.getDeadline());
        project.setPriority(request.getPriority());
        project.setAssignedConsultant(request.getAssignedConsultant());
        project.setMigrationPath(request.getMigrationPath());
        project.setStatus(ProjectStatus.PLANNED);
        project.setClient(client);

        return consultingProjectRepository.save(project);
    }

    @Transactional
    public ConsultingProject updateStatus(Long id, ProjectStatus status) {
        ConsultingProject project = consultingProjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
        project.setStatus(status);
        return consultingProjectRepository.save(project);
    }
}
