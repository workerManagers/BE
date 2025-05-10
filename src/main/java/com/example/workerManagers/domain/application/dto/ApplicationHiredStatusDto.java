package com.example.workerManagers.domain.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationHiredStatusDto {
    private Long applicationId;
    private Long userId;
    private String userName;
    private Long jobPostId;
    private String companyName;
    private boolean isHired;
    private String message;

    public static ApplicationHiredStatusDto from(ApplicationResponseDto application) {
        return ApplicationHiredStatusDto.builder()
                .applicationId(application.getApplicationId())
                .userId(application.getUserId())
                .userName(application.getUserName())
                .jobPostId(application.getJobPostId())
                .companyName(application.getCompanyName())
                .isHired("HIRED".equals(application.getStatus()))
                .message("HIRED".equals(application.getStatus()) ? "채용되었습니다." : "채용되지 않았습니다.")
                .build();
    }
} 