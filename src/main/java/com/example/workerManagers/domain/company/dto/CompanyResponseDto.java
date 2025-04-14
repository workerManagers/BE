package com.example.workerManagers.domain.company.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyResponseDto {
    private Long companyId;
    private String companyName;
    private String companyRegion;
    private String companyCode;
    private String message;
} 