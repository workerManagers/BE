package com.example.workerManagers.domain.jobpost.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JobPostResponseDto {
    private Long jobPostId;
    private Long industrialAccidentId;
    private Long companyId;
    private Long jobCodeId;
    private String jobPostDescription;
    private String jobPeriod;
    private LocalDateTime deadline;
    private String message;
} 