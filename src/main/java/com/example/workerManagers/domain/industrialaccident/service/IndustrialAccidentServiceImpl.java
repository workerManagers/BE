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
            Company company = companyRepository.findById(requestDto.getCompanyId())
                    .orElseThrow(() -> {
                        logger.warn("회사 조회 실패: ID={}", requestDto.getCompanyId());
                        return new CompanyException("회사를 찾을 수 없습니다.");
                    });

            // 직종 코드 조회
            JobCode jobCode = jobCodeRepository.findById(requestDto.getJobCodeId())
                    .orElseThrow(() -> {
                        logger.warn("직종 코드 조회 실패: ID={}", requestDto.getJobCodeId());
                        return new JobCodeException("직종 코드를 찾을 수 없습니다.");
                    });

            // 산업재해 엔티티 생성
            IndustrialAccident industrialAccident = IndustrialAccident.builder()
                    .company(company)
                    .jobCode(jobCode)
                    .industrialAccidentCode(requestDto.getIndustrialAccidentCode())
                    .industrialAccidentName(requestDto.getIndustrialAccidentName())
                    .industrialAccidentDate(requestDto.getIndustrialAccidentDate())
                    .jobPosts(new HashSet<>())
                    .aiMatchings(new HashSet<>())
                    .applications(new HashSet<>())
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
} 