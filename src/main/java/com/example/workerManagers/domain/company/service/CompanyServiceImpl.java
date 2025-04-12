package com.example.workerManagers.domain.company.service;

import com.example.workerManagers.domain.company.dto.CompanyRequestDto;
import com.example.workerManagers.domain.company.dto.CompanyResponseDto;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.global.exception.CompanyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) {
        Company company = Company.builder()
                .companyRegion(requestDto.getCompanyRegion())
                .companyCode(requestDto.getCompanyCode())
                .industrialAccidents(new HashSet<>())
                .restPeriods(new HashSet<>())
                .jobPosts(new HashSet<>())
                .aiMatchings(new HashSet<>())
                .applications(new HashSet<>())
                .resumes(new HashSet<>())
                .build();

        Company savedCompany = companyRepository.save(company);

        return CompanyResponseDto.builder()
                .companyId(savedCompany.getCompanyId())
                .companyRegion(savedCompany.getCompanyRegion())
                .companyCode(savedCompany.getCompanyCode())
                .message("회사가 성공적으로 생성되었습니다.")
                .build();
    }

    @Override
    public CompanyResponseDto getCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException("회사를 찾을 수 없습니다."));

        return CompanyResponseDto.builder()
                .companyId(company.getCompanyId())
                .companyRegion(company.getCompanyRegion())
                .companyCode(company.getCompanyCode())
                .message("회사 조회가 완료되었습니다.")
                .build();
    }
} 