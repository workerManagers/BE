package com.example.workerManagers.domain.jobcode.repository;

import com.example.workerManagers.domain.jobcode.entity.JobCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCodeRepository extends JpaRepository<JobCode, Long> {
} 