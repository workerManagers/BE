package com.example.workerManagers.domain.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationResponseDto {
    private Long applicationId;
    private Long companyId;
    private Long industrialAccidentId;
    private Long jobCodeId;
    private Long jobPostId;
    private Long userId;
    private Long aiMatchingId;
    private String jobDescription;
    private String jobPeriod;
    private String applyResult;
    private String message;
} 