package com.example.workerManagers.domain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDto {
    private Long applicationId;
    private String companyName;
    private String jobName;
    private Long jobPostId;
    private Long userId;
    private Long aiMatchingId;
    private String jobDescription;
    private String jobPeriod;
    private String applyResult;
    private String message;
} 