package com.example.workerManagers.domain.talentbookmark.repository;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.talentbookmark.entity.TalentBookmark;
import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TalentBookmarkRepository extends JpaRepository<TalentBookmark, Long> {
    List<TalentBookmark> findByCompany(Company company);
    Optional<TalentBookmark> findByCompanyAndUser(Company company, User user);
    boolean existsByCompanyAndUser(Company company, User user);
} 