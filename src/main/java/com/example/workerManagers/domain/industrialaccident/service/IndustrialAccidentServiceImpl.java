package com.example.workerManagers.domain.industrialaccident.service;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.exception.CompanyException;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentRequestDto;
import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentResponseDto;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.industrialaccident.exception.IndustrialAccidentException;
import com.example.workerManagers.domain.industrialaccident.repository.IndustrialAccidentRepository;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobcode.exception.JobCodeException;
import com.example.workerManagers.domain.jobcode.repository.JobCodeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndustrialAccidentServiceImpl implements IndustrialAccidentService {

    private final IndustrialAccidentRepository industrialAccidentRepository;
    private final CompanyRepository companyRepository;
    private final JobCodeRepository jobCodeRepository;
    private static final Logger logger = LoggerFactory.getLogger(IndustrialAccidentServiceImpl.class);

    @Override
    @Transactional
    public IndustrialAccidentResponseDto createIndustrialAccident(IndustrialAccidentRequestDto requestDto) {
        logger.info("산업재해 생성을 시작합니다.");
        try {
            // 회사 조회
            Company company = companyRepository.findByCompanyName(requestDto.getCompanyName())
                    .orElseThrow(() -> new CompanyException("Company not found"));

            // 직종 코드 조회
            JobCode jobCode = jobCodeRepository.findByJobName(requestDto.getJobName())
                    .orElseThrow(() -> new JobCodeException("JobCode not found"));

            // 산업재해 엔티티 생성
            IndustrialAccident industrialAccident = IndustrialAccident.builder()
                    .company(company)
                    .jobCode(jobCode)
                    .industrialAccidentCode(requestDto.getIndustrialAccidentCode())
                    .industrialAccidentName(requestDto.getIndustrialAccidentName())
                    .industrialAccidentDate(requestDto.getIndustrialAccidentDate())
                    .build();

            // 산업재해 저장
            IndustrialAccident savedIndustrialAccident = industrialAccidentRepository.save(industrialAccident);
            logger.info("산업재해가 성공적으로 생성되었습니다: ID={}", savedIndustrialAccident.getIndustrialAccidentId());

            // 응답 DTO 생성
            return IndustrialAccidentResponseDto.of(savedIndustrialAccident, "산업재해가 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            logger.error("산업재해 생성 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public IndustrialAccidentResponseDto getIndustrialAccident(Long industrialAccidentId) {
        logger.info("산업재해 조회를 시작합니다: ID={}", industrialAccidentId);
        try {
            // 산업재해 조회
            IndustrialAccident industrialAccident = industrialAccidentRepository.findById(industrialAccidentId)
                    .orElseThrow(() -> {
                        logger.warn("산업재해 조회 실패: ID={}", industrialAccidentId);
                        return new IndustrialAccidentException("산업재해를 찾을 수 없습니다.");
                    });

            // 응답 DTO 생성
            return IndustrialAccidentResponseDto.of(industrialAccident, "산업재해가 성공적으로 조회되었습니다.");
        } catch (Exception e) {
            logger.error("산업재해 조회 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<IndustrialAccidentResponseDto> getAllIndustrialAccidents() {
        List<IndustrialAccident> industrialAccidents = industrialAccidentRepository.findAll();
        return industrialAccidents.stream()
                .map(industrialAccident -> IndustrialAccidentResponseDto.builder()
                        .industrialAccidentId(industrialAccident.getIndustrialAccidentId())
                        .companyName(industrialAccident.getCompany().getCompanyName())
                        .jobName(industrialAccident.getJobCode().getJobName())
                        .industrialAccidentCode(industrialAccident.getIndustrialAccidentCode())
                        .industrialAccidentName(industrialAccident.getIndustrialAccidentName())
                        .industrialAccidentDate(industrialAccident.getIndustrialAccidentDate())
                        .message("모든 산업재해 조회가 완료되었습니다.")
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public IndustrialAccidentResponseDto updateIndustrialAccident(Long industrialAccidentId, IndustrialAccidentRequestDto requestDto) {
        IndustrialAccident industrialAccident = industrialAccidentRepository.findById(industrialAccidentId)
                .orElseThrow(() -> new IndustrialAccidentException("산업재해를 찾을 수 없습니다."));

        Company company = companyRepository.findByCompanyName(requestDto.getCompanyName())
                .orElseThrow(() -> new CompanyException("Company not found"));
        
        JobCode jobCode = jobCodeRepository.findByJobName(requestDto.getJobName())
                .orElseThrow(() -> new JobCodeException("JobCode not found"));

        industrialAccident.update(company, jobCode, requestDto.getIndustrialAccidentCode(),
                requestDto.getIndustrialAccidentName(), requestDto.getIndustrialAccidentDate());

        return IndustrialAccidentResponseDto.builder()
                .industrialAccidentId(industrialAccident.getIndustrialAccidentId())
                .companyName(industrialAccident.getCompany().getCompanyName())
                .jobName(industrialAccident.getJobCode().getJobName())
                .industrialAccidentCode(industrialAccident.getIndustrialAccidentCode())
                .industrialAccidentName(industrialAccident.getIndustrialAccidentName())
                .industrialAccidentDate(industrialAccident.getIndustrialAccidentDate())
                .message("산업재해가 성공적으로 수정되었습니다.")
                .build();
    }
} 