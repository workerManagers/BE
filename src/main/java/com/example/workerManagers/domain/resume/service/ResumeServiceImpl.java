package com.example.workerManagers.domain.resume.service;

import com.example.workerManagers.domain.resume.dto.ResumeRequestDto;
import com.example.workerManagers.domain.resume.dto.ResumeResponseDto;
import com.example.workerManagers.domain.resume.entity.Resume;
import com.example.workerManagers.domain.resume.repository.ResumeRepository;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    @Override
    public ResumeResponseDto createResume(ResumeRequestDto requestDto, String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 이미 자소서가 있는지 확인
        if (resumeRepository.existsByUser(user)) {
            throw new IllegalStateException("User already has a resume");
        }

        Resume resume = Resume.builder()
                .user(user)
                .resumeText(requestDto.getResumeText())
                .build();

        Resume savedResume = resumeRepository.save(resume);

        return ResumeResponseDto.builder()
                .resumeId(savedResume.getResumeId())
                .resumeText(savedResume.getResumeText())
                .userName(user.getUserName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeResponseDto getResume(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found"));

        return ResumeResponseDto.builder()
                .resumeId(resume.getResumeId())
                .resumeText(resume.getResumeText())
                .userName(user.getUserName())
                .build();
    }

    @Override
    public ResumeResponseDto updateResume(ResumeRequestDto requestDto, String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found"));

        resume.updateResumeText(requestDto.getResumeText());

        return ResumeResponseDto.builder()
                .resumeId(resume.getResumeId())
                .resumeText(resume.getResumeText())
                .userName(user.getUserName())
                .build();
    }

    @Override
    public void deleteResume(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found"));

        resumeRepository.delete(resume);
    }
} 