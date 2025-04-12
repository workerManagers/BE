package com.example.workerManagers.domain.jobpost.service;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.industrialaccident.repository.IndustrialAccidentRepository;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobcode.repository.JobCodeRepository;
import com.example.workerManagers.domain.jobpost.dto.JobPostRequestDto;
import com.example.workerManagers.domain.jobpost.dto.JobPostResponseDto;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.exception.JobPostException;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.company.exception.CompanyException;
import com.example.workerManagers.domain.industrialaccident.exception.IndustrialAccidentException;
import com.example.workerManagers.domain.jobcode.exception.JobCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final CompanyRepository companyRepository;
    private final IndustrialAccidentRepository industrialAccidentRepository;
    private final JobCodeRepository jobCodeRepository;

    @Override
    @Transactional
    public JobPostResponseDto createJobPost(JobPostRequestDto requestDto) {
        Company company = companyRepository.findById(requestDto.getCompanyId())
                .orElseThrow(() -> new CompanyException("Company not found"));

        IndustrialAccident industrialAccident = industrialAccidentRepository.findById(requestDto.getIndustrialAccidentId())
                .orElseThrow(() -> new IndustrialAccidentException("IndustrialAccident not found"));

        JobCode jobCode = jobCodeRepository.findById(requestDto.getJobCodeId())
                .orElseThrow(() -> new JobCodeException("JobCode not found"));

        JobPost jobPost = JobPost.builder()
                .company(company)
                .industrialAccident(industrialAccident)
                .jobCode(jobCode)
                .jobPostDescription(requestDto.getJobPostDescription())
                .jobPeriod(requestDto.getJobPeriod())
                .deadline(requestDto.getDeadline())
                .applications(new HashSet<>())
                .aiMatchings(new HashSet<>())
                .resumes(new HashSet<>())
                .build();

        JobPost savedJobPost = jobPostRepository.save(jobPost);

        return JobPostResponseDto.builder()
                .jobPostId(savedJobPost.getJobPostId())
                .companyId(savedJobPost.getCompany().getCompanyId())
                .industrialAccidentId(savedJobPost.getIndustrialAccident().getIndustrialAccidentId())
                .jobCodeId(savedJobPost.getJobCode().getJobCodeId())
                .jobPostDescription(savedJobPost.getJobPostDescription())
                .jobPeriod(savedJobPost.getJobPeriod())
                .deadline(savedJobPost.getDeadline())
                .message("모집공고가 성공적으로 생성되었습니다.")
                .build();
    }

    @Override
    public JobPostResponseDto getJobPost(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new JobPostException("JobPost not found"));

        return JobPostResponseDto.builder()
                .jobPostId(jobPost.getJobPostId())
                .companyId(jobPost.getCompany().getCompanyId())
                .industrialAccidentId(jobPost.getIndustrialAccident().getIndustrialAccidentId())
                .jobCodeId(jobPost.getJobCode().getJobCodeId())
                .jobPostDescription(jobPost.getJobPostDescription())
                .jobPeriod(jobPost.getJobPeriod())
                .deadline(jobPost.getDeadline())
                .message("모집공고 조회가 완료되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public JobPostResponseDto updateJobPost(Long jobPostId, JobPostRequestDto requestDto) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new JobPostException("모집공고를 찾을 수 없습니다."));

        Company company = companyRepository.findById(requestDto.getCompanyId())
                .orElseThrow(() -> new CompanyException("Company not found"));
        
        IndustrialAccident industrialAccident = industrialAccidentRepository.findById(requestDto.getIndustrialAccidentId())
                .orElseThrow(() -> new IndustrialAccidentException("IndustrialAccident not found"));
        
        JobCode jobCode = jobCodeRepository.findById(requestDto.getJobCodeId())
                .orElseThrow(() -> new JobCodeException("JobCode not found"));

        jobPost.update(company, industrialAccident, jobCode, requestDto.getJobPostDescription(),
                requestDto.getJobPeriod(), requestDto.getDeadline());

        return JobPostResponseDto.builder()
                .jobPostId(jobPost.getJobPostId())
                .companyId(jobPost.getCompany().getCompanyId())
                .industrialAccidentId(jobPost.getIndustrialAccident().getIndustrialAccidentId())
                .jobCodeId(jobPost.getJobCode().getJobCodeId())
                .jobPostDescription(jobPost.getJobPostDescription())
                .jobPeriod(jobPost.getJobPeriod())
                .deadline(jobPost.getDeadline())
                .message("모집공고가 성공적으로 수정되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public void deleteJobPost(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new JobPostException("모집공고를 찾을 수 없습니다."));
        
        jobPostRepository.delete(jobPost);
    }
} 