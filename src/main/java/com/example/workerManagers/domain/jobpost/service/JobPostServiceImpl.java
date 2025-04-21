package com.example.workerManagers.domain.jobpost.service;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobcode.repository.JobCodeRepository;
import com.example.workerManagers.domain.jobpost.dto.JobPostRequestDto;
import com.example.workerManagers.domain.jobpost.dto.JobPostResponseDto;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.exception.JobPostException;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.company.exception.CompanyException;
import com.example.workerManagers.domain.jobcode.exception.JobCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final CompanyRepository companyRepository;
    private final JobCodeRepository jobCodeRepository;

    @Override
    @Transactional
    public JobPostResponseDto createJobPost(JobPostRequestDto requestDto) {
        Company company = companyRepository.findByCompanyName(requestDto.getCompanyName())
                .orElseThrow(() -> new CompanyException("회사를 찾을 수 없습니다: " + requestDto.getCompanyName()));

        JobCode jobCode = jobCodeRepository.findByJobName(requestDto.getJobName())
                .orElseThrow(() -> new JobCodeException("직종 코드를 찾을 수 없습니다: " + requestDto.getJobName()));

        JobPost jobPost = JobPost.builder()
                .company(company)
                .jobCode(jobCode)
                .jobPostDescription(requestDto.getJobPostDescription())
                .mainTasks(requestDto.getMainTasks())
                .qualifications(requestDto.getQualifications())
                .preferredQualifications(requestDto.getPreferredQualifications())
                .idealCandidate(requestDto.getIdealCandidate())
                .jobPeriod(requestDto.getJobPeriod())
                .jobRegion(requestDto.getJobRegion())
                .deadline(requestDto.getDeadline())
                .build();

        JobPost savedJobPost = jobPostRepository.save(jobPost);

        return JobPostResponseDto.builder()
                .jobPostId(savedJobPost.getJobPostId())
                .companyName(savedJobPost.getCompany().getCompanyName())
                .jobName(savedJobPost.getJobCode().getJobName())
                .jobPostDescription(savedJobPost.getJobPostDescription())
                .mainTasks(savedJobPost.getMainTasks())
                .qualifications(savedJobPost.getQualifications())
                .preferredQualifications(savedJobPost.getPreferredQualifications())
                .idealCandidate(savedJobPost.getIdealCandidate())
                .jobPeriod(savedJobPost.getJobPeriod())
                .jobRegion(savedJobPost.getJobRegion())
                .deadline(savedJobPost.getDeadline())
                .message("채용 공고가 성공적으로 생성되었습니다.")
                .build();
    }

    @Override
    public JobPostResponseDto getJobPost(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new JobPostException("JobPost not found"));

        return JobPostResponseDto.of(jobPost, "모집공고 조회가 완료되었습니다.");
    }

    @Override
    @Transactional
    public JobPostResponseDto updateJobPost(Long jobPostId, JobPostRequestDto requestDto) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new JobPostException("채용 공고를 찾을 수 없습니다: " + jobPostId));

        Company company = companyRepository.findByCompanyName(requestDto.getCompanyName())
                .orElseThrow(() -> new CompanyException("회사를 찾을 수 없습니다: " + requestDto.getCompanyName()));
        
        JobCode jobCode = jobCodeRepository.findByJobName(requestDto.getJobName())
                .orElseThrow(() -> new JobCodeException("직종 코드를 찾을 수 없습니다: " + requestDto.getJobName()));

        jobPost.update(company, jobCode, requestDto.getJobPostDescription(),
                requestDto.getJobPeriod(), requestDto.getDeadline(),
                requestDto.getMainTasks(), requestDto.getQualifications(),
                requestDto.getPreferredQualifications(), requestDto.getIdealCandidate(),
                requestDto.getJobRegion());

        return JobPostResponseDto.builder()
                .jobPostId(jobPost.getJobPostId())
                .companyName(jobPost.getCompany().getCompanyName())
                .jobName(jobPost.getJobCode().getJobName())
                .jobPostDescription(jobPost.getJobPostDescription())
                .mainTasks(jobPost.getMainTasks())
                .qualifications(jobPost.getQualifications())
                .preferredQualifications(jobPost.getPreferredQualifications())
                .idealCandidate(jobPost.getIdealCandidate())
                .jobPeriod(jobPost.getJobPeriod())
                .jobRegion(jobPost.getJobRegion())
                .deadline(jobPost.getDeadline())
                .message("채용 공고가 성공적으로 수정되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public void deleteJobPost(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("모집공고를 찾을 수 없습니다."));
        jobPostRepository.delete(jobPost);
    }

    @Override
    public List<JobPostResponseDto> getAllJobPosts() {
        List<JobPost> jobPosts = jobPostRepository.findAll();
        return jobPosts.stream()
                .map(jobPost -> JobPostResponseDto.of(jobPost, "모든 모집공고 조회가 완료되었습니다."))
                .collect(Collectors.toList());
    }

    @Override
    public String convertToFastApiFormat(JobPostResponseDto jobPost) {
        StringBuilder description = new StringBuilder();
        
        // 공고 설명
        description.append(jobPost.getJobPostDescription()).append(". ");
        
        // 주요 업무
        description.append("주요 업무: ").append(jobPost.getMainTasks()).append(". ");
        
        // 자격 요건
        description.append("자격 요건: ").append(jobPost.getQualifications()).append(". ");
        
        // 우대 사항
        description.append("우대 사항: ").append(jobPost.getPreferredQualifications()).append(". ");
        
        // 인재상
        description.append("인재상: ").append(jobPost.getIdealCandidate()).append(". ");
        
        // 근무 기간
        description.append("근무 기간: ").append(jobPost.getJobPeriod()).append(". ");
        
        // 근무 지역
        description.append("근무 지역: ").append(jobPost.getJobRegion()).append(".");
        
        return description.toString();
    }
} 