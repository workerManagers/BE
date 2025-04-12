package com.example.workerManagers.domain.application.service;

import com.example.workerManagers.domain.application.dto.ApplicationRequestDto;
import com.example.workerManagers.domain.application.dto.ApplicationResponseDto;

public interface ApplicationService {
    ApplicationResponseDto createApplication(ApplicationRequestDto requestDto);
    ApplicationResponseDto getApplication(Long applicationId);
} 