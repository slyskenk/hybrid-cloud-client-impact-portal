package com.ibmjob.hybridportal.service;

import com.ibmjob.hybridportal.domain.ClientProfile;
import com.ibmjob.hybridportal.domain.ConsultingProject;
import com.ibmjob.hybridportal.domain.Milestone;
import com.ibmjob.hybridportal.domain.ProjectStatus;
import com.ibmjob.hybridportal.dto.MilestoneRequest;
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
        ConsultingProject project = findById(id);
        project.setStatus(status);
        return consultingProjectRepository.save(project);
    }

    @Transactional
    public ConsultingProject addMilestone(Long projectId, MilestoneRequest request) {
        ConsultingProject project = findById(projectId);
        Milestone milestone = new Milestone(request.getTitle(), request.getTargetDate(), request.isComplete());
        project.addMilestone(milestone);
        return consultingProjectRepository.save(project);
    }

    @Transactional
    public ConsultingProject updateMilestoneCompletion(Long projectId, Long milestoneId, boolean complete) {
        ConsultingProject project = findById(projectId);
        Milestone milestone = project.getMilestones().stream()
                .filter(candidate -> milestoneId.equals(candidate.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Milestone not found: " + milestoneId));
        milestone.setComplete(complete);
        return consultingProjectRepository.save(project);
    }

    private ConsultingProject findById(Long id) {
        return consultingProjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }
}
