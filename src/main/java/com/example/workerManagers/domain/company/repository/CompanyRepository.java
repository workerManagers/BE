package com.example.workerManagers.domain.company.repository;

import com.example.workerManagers.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCompanyName(String companyName);
} 