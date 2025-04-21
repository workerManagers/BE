package com.example.workerManagers.domain.aimatching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIMatchingResponseDto {
    private Long matchingId;
    private String companyName;
    private String jobName;
    private Long jobPostId;
    private Long userId;
    private Double matchingScore;
    private String message;
} 