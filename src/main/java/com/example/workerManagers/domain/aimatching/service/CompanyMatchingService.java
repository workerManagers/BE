package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingResponseDto;

import java.util.List;

public interface CompanyMatchingService {
    List<CompanyMatchingResponseDto> getMatchingScoresForResumes(CompanyMatchingRequestDto requestDto);
} 