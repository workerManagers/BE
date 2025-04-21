package com.example.workerManagers.domain.jobpost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastApiRequestDto {
    private String inputText;
    private List<JobPostDatasetDto> dataset;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobPostDatasetDto {
        private Long jobPostId;
        private String jobPostDescription;
    }
} 