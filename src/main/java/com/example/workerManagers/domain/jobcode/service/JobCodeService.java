package com.example.workerManagers.domain.jobcode.service;

import com.example.workerManagers.domain.jobcode.dto.JobCodeRequestDto;
import com.example.workerManagers.domain.jobcode.dto.JobCodeResponseDto;

public interface JobCodeService {
    JobCodeResponseDto createJobCode(JobCodeRequestDto requestDto);
    JobCodeResponseDto getJobCode(Long jobCodeId);
} 