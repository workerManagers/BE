package com.example.workerManagers.domain.jobpost.service;

import com.example.workerManagers.domain.jobpost.dto.JobPostRequestDto;
import com.example.workerManagers.domain.jobpost.dto.JobPostResponseDto;
import java.util.List;

public interface JobPostService {
    JobPostResponseDto createJobPost(JobPostRequestDto requestDto);
    JobPostResponseDto getJobPost(Long jobPostId);
    JobPostResponseDto updateJobPost(Long jobPostId, JobPostRequestDto requestDto);
    void deleteJobPost(Long jobPostId);
    List<JobPostResponseDto> getAllJobPosts();
} 