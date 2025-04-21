package com.example.workerManagers.domain.application.dto;

import com.example.workerManagers.domain.application.entity.Application;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponseDto {
    private Long applicationId;
    private String userName;
    private Long jobPostId;
    private String companyName;
    private String status;
    private LocalDateTime appliedAt;

    public static ApplicationResponseDto from(Application application) {
        return ApplicationResponseDto.builder()
                .applicationId(application.getApplicationId())
                .userName(application.getUser().getUserName())
                .jobPostId(application.getJobPost().getJobPostId())
                .companyName(application.getJobPost().getCompany().getCompanyName())
                .status(application.getStatus().name())
                .appliedAt(application.getAppliedAt())
                .build();
    }
} 