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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) {
        Company company = Company.builder()
                .companyName(requestDto.getCompanyName())
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
                .companyName(savedCompany.getCompanyName())
                .companyRegion(savedCompany.getCompanyRegion())
                .companyCode(savedCompany.getCompanyCode())
                .message("회사가 성공적으로 생성되었습니다.")
                .build();
    }

    @Override
    public CompanyResponseDto getCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException("Company not found"));

        return CompanyResponseDto.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .companyRegion(company.getCompanyRegion())
                .companyCode(company.getCompanyCode())
                .message("회사 조회가 완료되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public CompanyResponseDto updateCompany(Long companyId, CompanyRequestDto requestDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException("Company not found"));

        company.update(requestDto.getCompanyName(), requestDto.getCompanyRegion(), requestDto.getCompanyCode());

        return CompanyResponseDto.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .companyRegion(company.getCompanyRegion())
                .companyCode(company.getCompanyCode())
                .message("회사가 성공적으로 수정되었습니다.")
                .build();
    }

    @Override
    public List<CompanyResponseDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> CompanyResponseDto.builder()
                        .companyId(company.getCompanyId())
                        .companyName(company.getCompanyName())
                        .companyRegion(company.getCompanyRegion())
                        .companyCode(company.getCompanyCode())
                        .message("모든 회사 조회가 완료되었습니다.")
                        .build())
                .collect(Collectors.toList());
    }
} 