package com.example.workerManagers.domain.jobpost.dto;

import com.example.workerManagers.domain.jobpost.entity.JobPost;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class JobPostResponseDto {
    private Long jobPostId;
    private String industrialAccidentName;
    private String companyName;
    private String companyRegion;
    private String jobName;
    private String jobPostDescription;
    private String jobPeriod;
    private LocalDateTime deadline;
    private String message;

    public static JobPostResponseDto of(JobPost jobPost, String message) {
        return JobPostResponseDto.builder()
                .jobPostId(jobPost.getJobPostId())
                .industrialAccidentName(jobPost.getIndustrialAccident().getIndustrialAccidentName())
                .companyName(jobPost.getCompany().getCompanyName())
                .companyRegion(jobPost.getCompany().getCompanyRegion())
                .jobName(jobPost.getJobCode().getJobName())
                .jobPostDescription(jobPost.getJobPostDescription())
                .jobPeriod(jobPost.getJobPeriod())
                .deadline(jobPost.getDeadline())
                .message(message)
                .build();
    }
} 