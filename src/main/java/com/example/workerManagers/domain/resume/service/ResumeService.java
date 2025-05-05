package com.example.workerManagers.domain.resume.service;

import com.example.workerManagers.domain.resume.dto.ResumeRequestDto;
import com.example.workerManagers.domain.resume.dto.ResumeResponseDto;
import java.util.List;

public interface ResumeService {
    ResumeResponseDto createResume(ResumeRequestDto requestDto, String userEmail);
    ResumeResponseDto getResume(String userEmail);
    ResumeResponseDto updateResume(ResumeRequestDto requestDto, String userEmail);
    void deleteResume(String userEmail);
    ResumeResponseDto getResumeById(Long resumeId, String userEmail);
    ResumeResponseDto getResumeByUserId(Long userId);
    List<ResumeResponseDto> getAllMatchingEnabledResumes();
} 