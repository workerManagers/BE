package com.example.workerManagers.domain.aimatching.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPostMatchingDto {
    private Long jobPostId;
    private String companyName;
    private String jobName;
    private String jobPostDescription;
    private Double matchingScore;
} 