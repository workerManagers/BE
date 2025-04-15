package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.AIMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.AIMatchingResponseDto;

public interface AIMatchingService {
    AIMatchingResponseDto createAIMatching(AIMatchingRequestDto requestDto);
    AIMatchingResponseDto getAIMatching(Long matchingId);
} 