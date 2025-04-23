package com.example.workerManagers.domain.jobcode.service;

import com.example.workerManagers.domain.jobcode.dto.JobCodeRequestDto;
import com.example.workerManagers.domain.jobcode.dto.JobCodeResponseDto;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobcode.exception.JobCodeException;
import com.example.workerManagers.domain.jobcode.repository.JobCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobCodeServiceImpl implements JobCodeService {

    private final JobCodeRepository jobCodeRepository;

    @Override
    @Transactional
    public JobCodeResponseDto createJobCode(JobCodeRequestDto requestDto) {
        JobCode jobCode = JobCode.builder()
                .jobCode(requestDto.getJobCode())
                .jobName(requestDto.getJobName())
                .industryCategory(requestDto.getIndustryCategory())
                .industrySubcategory(requestDto.getIndustrySubcategory())
                .restPeriods(new HashSet<>())
                .industrialAccidents(new HashSet<>())
                .applications(new HashSet<>())
                .jobPosts(new HashSet<>())
                .build();

        JobCode savedJobCode = jobCodeRepository.save(jobCode);

        return JobCodeResponseDto.builder()
                .jobCodeId(savedJobCode.getJobCodeId())
                .jobCode(savedJobCode.getJobCode())
                .jobName(savedJobCode.getJobName())
                .industryCategory(savedJobCode.getIndustryCategory())
                .industrySubcategory(savedJobCode.getIndustrySubcategory())
                .message("직종 코드가 성공적으로 생성되었습니다.")
                .build();
    }

    @Override
    public JobCodeResponseDto getJobCode(Long jobCodeId) {
        JobCode jobCode = jobCodeRepository.findById(jobCodeId)
                .orElseThrow(() -> new JobCodeException("직종 코드를 찾을 수 없습니다."));

        return JobCodeResponseDto.builder()
                .jobCode(jobCode.getJobCode())
                .jobName(jobCode.getJobName())
                .message("직종 코드 조회가 완료되었습니다.")
                .build();
    }

    @Override
    public List<JobCodeResponseDto> getAllJobCodes() {
        List<JobCode> jobCodes = jobCodeRepository.findAll();
        return jobCodes.stream()
                .map(jobCode -> JobCodeResponseDto.builder()
                        .jobCodeId(jobCode.getJobCodeId())
                        .jobCode(jobCode.getJobCode())
                        .jobName(jobCode.getJobName())
                        .message("직종 코드 조회가 완료되었습니다.")
                        .build())
                .collect(Collectors.toList());
    }
} 