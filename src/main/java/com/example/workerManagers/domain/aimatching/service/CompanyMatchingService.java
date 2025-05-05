package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingResponseDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CompanyMatchingService {
    CompletableFuture<List<CompanyMatchingResponseDto>> getMatchingScoresForResumes(CompanyMatchingRequestDto requestDto);
} 