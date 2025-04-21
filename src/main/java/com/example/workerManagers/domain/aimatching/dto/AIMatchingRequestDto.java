package com.example.workerManagers.domain.aimatching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIMatchingRequestDto {
    private String input_text;
    private List<JobPostData> dataset;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobPostData {
        private Long jobPost_id;
        private String jobPost_description;
    }
} 