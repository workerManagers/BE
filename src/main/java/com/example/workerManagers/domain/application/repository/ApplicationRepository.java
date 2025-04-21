package com.example.workerManagers.domain.application.repository;

import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.application.entity.ApplicationStatus;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser(User user);
    List<Application> findByJobPost(JobPost jobPost);
    List<Application> findByStatus(ApplicationStatus status);
    Optional<Application> findByUserAndJobPost(User user, JobPost jobPost);
    boolean existsByUserAndJobPost(User user, JobPost jobPost);
} 