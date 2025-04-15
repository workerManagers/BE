package com.example.workerManagers.domain.aimatching.repository;

import com.example.workerManagers.domain.aimatching.entity.AIMatching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIMatchingRepository extends JpaRepository<AIMatching, Long> {
} 