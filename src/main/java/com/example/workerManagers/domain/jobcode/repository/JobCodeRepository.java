package com.example.workerManagers.domain.jobcode.repository;

import com.example.workerManagers.domain.jobcode.entity.JobCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobCodeRepository extends JpaRepository<JobCode, Long> {
    Optional<JobCode> findByJobName(String jobName);
} 