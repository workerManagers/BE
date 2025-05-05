package com.example.workerManagers.domain.resume.repository;

import com.example.workerManagers.domain.resume.entity.Resume;
import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByUser(User user);
    boolean existsByUser(User user);
    
    @Query("SELECT r FROM Resume r WHERE r.user.matchingEnabled = true")
    List<Resume> findAllByMatchingEnabled();
} 