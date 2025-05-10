package com.example.workerManagers.domain.application.service;

import com.example.workerManagers.domain.application.dto.ApplicationRequestDto;
import com.example.workerManagers.domain.application.dto.ApplicationResponseDto;
import com.example.workerManagers.domain.application.dto.ApplicationStatusUpdateDto;
import com.example.workerManagers.domain.application.dto.ApplicationHiredStatusDto;

import java.util.List;

public interface ApplicationService {
    ApplicationResponseDto createApplication(ApplicationRequestDto requestDto, String userEmail);
    List<ApplicationResponseDto> getApplicationsByUser(String userEmail);
    List<ApplicationResponseDto> getApplicationsByJobPost(Long jobPostId);
    List<ApplicationResponseDto> getJobPostApplications(Long jobPostId, String userEmail);
    List<ApplicationResponseDto> getMyApplications(String userEmail);
    ApplicationResponseDto updateApplicationStatus(Long applicationId, ApplicationStatusUpdateDto requestDto, String userEmail);
    void deleteApplication(Long applicationId, String userEmail);
    ApplicationHiredStatusDto getApplicationHiredStatus(Long applicationId, String userEmail);
} 