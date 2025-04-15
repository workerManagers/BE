package com.example.workerManagers.domain.company.service;

import com.example.workerManagers.domain.company.dto.CompanyRequestDto;
import com.example.workerManagers.domain.company.dto.CompanyResponseDto;

import java.util.List;

public interface CompanyService {
    CompanyResponseDto createCompany(CompanyRequestDto requestDto);
    CompanyResponseDto getCompany(Long companyId);
    CompanyResponseDto updateCompany(Long companyId, CompanyRequestDto requestDto);
    List<CompanyResponseDto> getAllCompanies();
} 