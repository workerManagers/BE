package com.example.workerManagers.domain.jobcode.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobCodeResponseDto {
    private Long jobCodeId;
    private String jobCode;
    private String jobName;
    private String jobDescription;
    private String message;
} 