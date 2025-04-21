package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.AIMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.AIMatchingResponseDto;
import com.example.workerManagers.domain.aimatching.dto.JobPostMatchingDto;
import java.util.List;

public interface AIMatchingService {
    List<JobPostMatchingDto> getMatchingScoresForAllJobPosts(String resumeText);
    List<AIMatchingResponseDto> getMatchingScores(AIMatchingRequestDto requestDto);
} 