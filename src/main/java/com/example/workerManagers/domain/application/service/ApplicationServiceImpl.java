package com.example.workerManagers.domain.application.service;

import com.example.workerManagers.domain.application.dto.ApplicationRequestDto;
import com.example.workerManagers.domain.application.dto.ApplicationResponseDto;
import com.example.workerManagers.domain.application.dto.ApplicationStatusUpdateDto;
import com.example.workerManagers.domain.application.dto.ApplicationHiredStatusDto;
import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.application.repository.ApplicationRepository;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.resume.entity.Resume;
import com.example.workerManagers.domain.resume.repository.ResumeRepository;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.repository.UserRepository;
import com.example.workerManagers.domain.resume.exception.ResumeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobPostRepository jobPostRepository;
    private final ResumeRepository resumeRepository;

    @Override
    public ApplicationResponseDto createApplication(ApplicationRequestDto requestDto, String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResumeException("사용자를 찾을 수 없습니다."));

        JobPost jobPost = jobPostRepository.findById(requestDto.getJobPostId())
                .orElseThrow(() -> new ResumeException("채용 공고를 찾을 수 없습니다."));

        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new ResumeException("이력서를 찾을 수 없습니다."));

        if (applicationRepository.existsByUserAndJobPost(user, jobPost)) {
            throw new ResumeException("이미 지원한 채용 공고입니다.");
        }

        Application application = Application.builder()
                .user(user)
                .jobPost(jobPost)
                .resume(resume)
                .build();

        Application savedApplication = applicationRepository.save(application);
        return ApplicationResponseDto.from(savedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getApplicationsByUser(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResumeException("사용자를 찾을 수 없습니다."));

        return applicationRepository.findByUser(user).stream()
                .map(ApplicationResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getApplicationsByJobPost(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new ResumeException("채용 공고를 찾을 수 없습니다."));

        return applicationRepository.findByJobPost(jobPost).stream()
                .map(ApplicationResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getJobPostApplications(Long jobPostId, String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResumeException("사용자를 찾을 수 없습니다."));

        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new ResumeException("채용 공고를 찾을 수 없습니다."));

        if (!jobPost.getCompany().getUser().getUserEmail().equals(userEmail)) {
            throw new ResumeException("권한이 없습니다.");
        }

        return applicationRepository.findByJobPost(jobPost).stream()
                .map(ApplicationResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getMyApplications(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResumeException("사용자를 찾을 수 없습니다."));

        return applicationRepository.findByUser(user).stream()
                .map(ApplicationResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationResponseDto updateApplicationStatus(Long applicationId, ApplicationStatusUpdateDto requestDto, String userEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResumeException("지원서를 찾을 수 없습니다."));

        if (!application.getJobPost().getCompany().getUser().getUserEmail().equals(userEmail)) {
            throw new ResumeException("권한이 없습니다.");
        }

        application.updateStatus(requestDto.getStatus());

        return ApplicationResponseDto.from(application);
    }

    @Override
    public void deleteApplication(Long applicationId, String userEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResumeException("지원서를 찾을 수 없습니다."));

        if (!application.getUser().getUserEmail().equals(userEmail)) {
            throw new ResumeException("권한이 없습니다.");
        }

        applicationRepository.delete(application);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationHiredStatusDto getApplicationHiredStatus(Long applicationId, String userEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResumeException("지원서를 찾을 수 없습니다."));

        // 지원자 본인이나 해당 공고의 회사만 조회 가능
        if (!application.getUser().getUserEmail().equals(userEmail) && 
            !application.getJobPost().getCompany().getUser().getUserEmail().equals(userEmail)) {
            throw new ResumeException("권한이 없습니다.");
        }

        return ApplicationHiredStatusDto.from(ApplicationResponseDto.from(application));
    }
} 