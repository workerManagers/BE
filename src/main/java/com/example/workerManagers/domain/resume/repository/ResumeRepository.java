package com.example.workerManagers.domain.resume.repository;

import com.example.workerManagers.domain.resume.entity.Resume;
import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByUser(User user);
    boolean existsByUser(User user);
} 