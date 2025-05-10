package com.example.workerManagers.domain.jobpost.service;

import com.example.workerManagers.domain.jobpost.dto.JobPostRequestDto;
import com.example.workerManagers.domain.jobpost.dto.JobPostResponseDto;
import com.example.workerManagers.domain.jobpost.dto.RecruitmentStatusUpdateDto;
import java.util.List;

public interface JobPostService {
    JobPostResponseDto createJobPost(JobPostRequestDto requestDto);
    JobPostResponseDto getJobPost(Long jobPostId);
    JobPostResponseDto updateJobPost(Long jobPostId, JobPostRequestDto requestDto, String userEmail);
    void deleteJobPost(Long jobPostId, String userEmail);
    List<JobPostResponseDto> getAllJobPosts();
    String convertToFastApiFormat(JobPostResponseDto jobPost);
    List<JobPostResponseDto> getJobPostsByUserId(Long userId);
    JobPostResponseDto updateRecruitmentStatus(Long jobPostId, RecruitmentStatusUpdateDto requestDto, String userEmail);
} 