package com.example.workerManagers.domain.jobcode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCodeResponseDto {
    private String jobCode;
    private String jobName;
    private String message;
} 