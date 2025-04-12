package com.example.workerManagers.domain.aimatching.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AIMatchingResponseDto {
    private Long matchingId;
    private Long companyId;
    private Long industrialAccidentId;
    private Long jobCodeId;
    private Long jobPostId;
    private Long userId;
    private Double matchingScore;
    private String message;
} 