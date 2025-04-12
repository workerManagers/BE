package com.example.workerManagers.domain.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequestDto {
    @NotBlank(message = "회사 지역은 필수 입력값입니다.")
    @Size(max = 100, message = "회사 지역은 100자를 초과할 수 없습니다.")
    private String companyRegion;
    
    @NotBlank(message = "회사 코드는 필수 입력값입니다.")
    @Size(max = 20, message = "회사 코드는 20자를 초과할 수 없습니다.")
    private String companyCode;
} 