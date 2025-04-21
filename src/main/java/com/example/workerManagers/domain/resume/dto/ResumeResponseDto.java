package com.example.workerManagers.domain.resume.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResumeResponseDto {
    private Long resumeId;
    private String resumeText;
    private String userName;
} 