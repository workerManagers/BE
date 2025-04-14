package com.example.workerManagers.domain.jobcode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCodeResponseDto {
    private Long jobCodeId;
    private String jobCode;
    private String jobName;
    private String message;
} 