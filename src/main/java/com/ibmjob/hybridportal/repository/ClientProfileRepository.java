package com.ibmjob.hybridportal.repository;

import com.ibmjob.hybridportal.domain.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {

    List<ClientProfile> findByNameContainingIgnoreCaseOrIndustryContainingIgnoreCase(String name, String industry);
}
