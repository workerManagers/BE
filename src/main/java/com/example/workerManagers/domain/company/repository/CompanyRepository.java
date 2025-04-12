package com.example.workerManagers.domain.company.repository;

import com.example.workerManagers.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
} 