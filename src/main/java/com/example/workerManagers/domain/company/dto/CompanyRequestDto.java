package com.example.workerManagers.domain.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequestDto {
    @NotBlank(message = "회사명은 필수 입력값입니다.")
    @Size(max = 100, message = "회사명은 최대 100자까지 입력 가능합니다.")
    private String companyName;

    @NotBlank(message = "회사 지역은 필수 입력값입니다.")
    @Size(max = 50, message = "회사 지역은 최대 50자까지 입력 가능합니다.")
    private String companyRegion;
    
    @Size(max = 20, message = "회사 코드는 최대 20자까지 입력 가능합니다.")
    private String companyCode;
} 