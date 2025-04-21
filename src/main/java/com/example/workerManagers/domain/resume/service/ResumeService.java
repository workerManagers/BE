package com.example.workerManagers.domain.resume.service;

import com.example.workerManagers.domain.resume.dto.ResumeRequestDto;
import com.example.workerManagers.domain.resume.dto.ResumeResponseDto;

public interface ResumeService {
    ResumeResponseDto createResume(ResumeRequestDto requestDto, String userEmail);
    ResumeResponseDto getResume(String userEmail);
    ResumeResponseDto updateResume(ResumeRequestDto requestDto, String userEmail);
    void deleteResume(String userEmail);
} 