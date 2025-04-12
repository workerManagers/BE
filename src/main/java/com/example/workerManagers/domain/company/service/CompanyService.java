package com.example.workerManagers.domain.company.service;

import com.example.workerManagers.domain.company.dto.CompanyRequestDto;
import com.example.workerManagers.domain.company.dto.CompanyResponseDto;

public interface CompanyService {
    CompanyResponseDto createCompany(CompanyRequestDto requestDto);
    CompanyResponseDto getCompany(Long companyId);
} 