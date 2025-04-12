package com.example.workerManagers.domain.application.repository;

import com.example.workerManagers.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
} 