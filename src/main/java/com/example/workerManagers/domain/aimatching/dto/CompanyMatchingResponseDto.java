package com.example.workerManagers.domain.aimatching.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompanyMatchingResponseDto {
    private Long resumeId;
    private String applicantName;
    private Double matchingScore;
    private String resumeText;
} 