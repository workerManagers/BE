package com.example.workerManagers.domain.jobpost.repository;

import com.example.workerManagers.domain.jobpost.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    @Query("SELECT j FROM JobPost j LEFT JOIN FETCH j.company LEFT JOIN FETCH j.jobCode WHERE j.jobPostId = :id")
    Optional<JobPost> findByIdWithRelationships(@Param("id") Long id);
} 