package com.ibmjob.hybridportal.repository;

import com.ibmjob.hybridportal.domain.ConsultingProject;
import com.ibmjob.hybridportal.domain.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConsultingProjectRepository extends JpaRepository<ConsultingProject, Long> {

    long countByStatus(ProjectStatus status);

    @Query("select distinct project from ConsultingProject project left join fetch project.client left join fetch project.milestones order by project.deadline asc")
    List<ConsultingProject> findAllByOrderByDeadlineAsc();

    @Override
    @Query("select distinct project from ConsultingProject project left join fetch project.client left join fetch project.milestones where project.id = :id")
    Optional<ConsultingProject> findById(@Param("id") Long id);
}
