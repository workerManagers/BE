package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.AIMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.AIMatchingResponseDto;
import com.example.workerManagers.domain.aimatching.entity.AIMatching;
import com.example.workerManagers.domain.aimatching.exception.AIMatchingException;
import com.example.workerManagers.domain.aimatching.repository.AIMatchingRepository;
import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.exception.CompanyException;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.industrialaccident.exception.IndustrialAccidentException;
import com.example.workerManagers.domain.industrialaccident.repository.IndustrialAccidentRepository;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobcode.exception.JobCodeException;
import com.example.workerManagers.domain.jobcode.repository.JobCodeRepository;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.exception.JobPostException;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.resume.entity.Resume;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.exception.UserException;
import com.example.workerManagers.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AIMatchingServiceImpl implements AIMatchingService {

    private final AIMatchingRepository aiMatchingRepository;
    private final CompanyRepository companyRepository;
    private final IndustrialAccidentRepository industrialAccidentRepository;
    private final JobCodeRepository jobCodeRepository;
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AIMatchingResponseDto createAIMatching(AIMatchingRequestDto requestDto) {
        Company company = companyRepository.findById(requestDto.getCompanyId())
                .orElseThrow(() -> new CompanyException("Company not found"));

        IndustrialAccident industrialAccident = industrialAccidentRepository.findById(requestDto.getIndustrialAccidentId())
                .orElseThrow(() -> new IndustrialAccidentException("IndustrialAccident not found"));

        JobCode jobCode = jobCodeRepository.findById(requestDto.getJobCodeId())
                .orElseThrow(() -> new JobCodeException("JobCode not found"));

        JobPost jobPost = jobPostRepository.findById(requestDto.getJobPostId())
                .orElseThrow(() -> new JobPostException("JobPost not found"));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserException("User not found"));

        AIMatching aiMatching = AIMatching.builder()
                .company(company)
                .industrialAccident(industrialAccident)
                .jobCode(jobCode)
                .jobPost(jobPost)
                .user(user)
                .matchingScore(requestDto.getMatchingScore())
                .applications(new HashSet<>())
                .resumes(new HashSet<>())
                .build();

        AIMatching savedAIMatching = aiMatchingRepository.save(aiMatching);

        return AIMatchingResponseDto.builder()
                .matchingId(savedAIMatching.getMatchingId())
                .companyId(savedAIMatching.getCompany().getCompanyId())
                .industrialAccidentId(savedAIMatching.getIndustrialAccident().getIndustrialAccidentId())
                .jobCodeId(savedAIMatching.getJobCode().getJobCodeId())
                .jobPostId(savedAIMatching.getJobPost().getJobPostId())
                .userId(savedAIMatching.getUser().getUserId())
                .matchingScore(savedAIMatching.getMatchingScore())
                .message("AI 매칭이 성공적으로 생성되었습니다.")
                .build();
    }

    @Override
    public AIMatchingResponseDto getAIMatching(Long matchingId) {
        AIMatching aiMatching = aiMatchingRepository.findById(matchingId)
                .orElseThrow(() -> new AIMatchingException("AIMatching not found"));

        return AIMatchingResponseDto.builder()
                .matchingId(aiMatching.getMatchingId())
                .companyId(aiMatching.getCompany().getCompanyId())
                .industrialAccidentId(aiMatching.getIndustrialAccident().getIndustrialAccidentId())
                .jobCodeId(aiMatching.getJobCode().getJobCodeId())
                .jobPostId(aiMatching.getJobPost().getJobPostId())
                .userId(aiMatching.getUser().getUserId())
                .matchingScore(aiMatching.getMatchingScore())
                .message("AI 매칭 조회가 완료되었습니다.")
                .build();
    }
} 