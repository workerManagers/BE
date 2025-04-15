package com.example.workerManagers.domain.jobpost.repository;

import com.example.workerManagers.domain.jobpost.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
} 