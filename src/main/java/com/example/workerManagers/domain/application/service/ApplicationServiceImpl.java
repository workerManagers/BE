package com.example.workerManagers.domain.application.service;

import com.example.workerManagers.domain.application.dto.ApplicationRequestDto;
import com.example.workerManagers.domain.application.dto.ApplicationResponseDto;
import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.application.exception.ApplicationException;
import com.example.workerManagers.domain.application.repository.ApplicationRepository;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobcode.repository.JobCodeRepository;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.repository.UserRepository;
import com.example.workerManagers.domain.company.exception.CompanyException;
import com.example.workerManagers.domain.jobcode.exception.JobCodeException;
import com.example.workerManagers.domain.jobpost.exception.JobPostException;
import com.example.workerManagers.domain.users.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final JobCodeRepository jobCodeRepository;
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ApplicationResponseDto createApplication(ApplicationRequestDto requestDto) {
        Company company = companyRepository.findByCompanyName(requestDto.getCompanyName())
                .orElseThrow(() -> new CompanyException("Company not found"));

        JobCode jobCode = jobCodeRepository.findByJobName(requestDto.getJobName())
                .orElseThrow(() -> new JobCodeException("JobCode not found"));

        JobPost jobPost = jobPostRepository.findById(requestDto.getJobPostId())
                .orElseThrow(() -> new JobPostException("JobPost not found"));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserException("User not found"));

        Application application = Application.builder()
                .company(company)
                .jobCode(jobCode)
                .jobPost(jobPost)
                .user(user)
                .jobDescription(requestDto.getJobDescription())
                .jobPeriod(requestDto.getJobPeriod())
                .applyResult(requestDto.getApplyResult())
                .build();

        Application savedApplication = applicationRepository.save(application);

        return ApplicationResponseDto.builder()
                .applicationId(savedApplication.getApplicationId())
                .companyName(savedApplication.getCompany().getCompanyName())
                .jobName(savedApplication.getJobCode().getJobName())
                .jobPostId(savedApplication.getJobPost().getJobPostId())
                .userId(savedApplication.getUser().getUserId())
                .jobDescription(savedApplication.getJobDescription())
                .jobPeriod(savedApplication.getJobPeriod())
                .applyResult(savedApplication.getApplyResult())
                .message("지원이 성공적으로 생성되었습니다.")
                .build();
    }

    @Override
    public ApplicationResponseDto getApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationException("Application not found"));

        return ApplicationResponseDto.builder()
                .applicationId(application.getApplicationId())
                .companyName(application.getCompany().getCompanyName())
                .jobName(application.getJobCode().getJobName())
                .jobPostId(application.getJobPost().getJobPostId())
                .userId(application.getUser().getUserId())
                .jobDescription(application.getJobDescription())
                .jobPeriod(application.getJobPeriod())
                .applyResult(application.getApplyResult())
                .message("지원 조회가 완료되었습니다.")
                .build();
    }
} 