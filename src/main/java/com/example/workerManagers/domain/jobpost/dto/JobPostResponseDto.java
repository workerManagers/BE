package com.example.workerManagers.domain.jobpost.dto;

import com.example.workerManagers.domain.jobpost.entity.JobPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostResponseDto {
    private Long jobPostId;
    private String companyName;
    private String jobName;
    private String jobPostDescription;
    private String jobPeriod;
    private String jobRegion;
    private LocalDateTime deadline;
    private String message;

    public static JobPostResponseDto of(JobPost jobPost, String message) {
        return JobPostResponseDto.builder()
                .jobPostId(jobPost.getJobPostId())
                .companyName(jobPost.getCompany().getCompanyName())
                .jobName(jobPost.getJobCode().getJobName())
                .jobPostDescription(jobPost.getJobPostDescription())
                .jobPeriod(jobPost.getJobPeriod())
                .jobRegion(jobPost.getJobRegion())
                .deadline(jobPost.getDeadline())
                .message(message)
                .build();
    }
} 