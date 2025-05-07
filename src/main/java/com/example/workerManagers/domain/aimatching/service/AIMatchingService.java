package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.AIMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.AIMatchingResponseDto;
import com.example.workerManagers.domain.aimatching.dto.JobPostMatchingDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AIMatchingService {
    CompletableFuture<List<JobPostMatchingDto>> getMatchingScoresForAllJobPosts(String resumeText, Long resumeId);
    CompletableFuture<List<AIMatchingResponseDto>> getMatchingScores(AIMatchingRequestDto requestDto);
} 