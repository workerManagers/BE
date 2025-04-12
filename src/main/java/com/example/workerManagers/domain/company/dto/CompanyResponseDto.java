package com.example.workerManagers.domain.company.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompanyResponseDto {
    private Long companyId;
    private String companyRegion;
    private String companyCode;
    private String message;
} 