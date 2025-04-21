package com.example.workerManagers.domain.company.repository;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCompanyName(String companyName);
    Optional<Company> findByUser(User user);
} 