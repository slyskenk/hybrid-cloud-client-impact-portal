package com.ibmjob.hybridportal.repository;

import com.ibmjob.hybridportal.domain.ConsultingProject;
import com.ibmjob.hybridportal.domain.ProjectStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultingProjectRepository extends JpaRepository<ConsultingProject, Long> {

    long countByStatus(ProjectStatus status);

    @EntityGraph(attributePaths = {"client"})
    List<ConsultingProject> findAllByOrderByDeadlineAsc();

    @Override
    @EntityGraph(attributePaths = {"client"})
    Optional<ConsultingProject> findById(Long id);
}
