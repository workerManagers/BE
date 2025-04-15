package com.example.workerManagers.domain.industrialaccident.dto;

import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class IndustrialAccidentResponseDto {
    private Long industrialAccidentId;
    private String companyName;
    private String jobName;
    private String industrialAccidentCode;
    private String industrialAccidentName;
    private LocalDateTime industrialAccidentDate;
    private String message;

    public static IndustrialAccidentResponseDto of(IndustrialAccident industrialAccident, String message) {
        return IndustrialAccidentResponseDto.builder()
                .industrialAccidentId(industrialAccident.getIndustrialAccidentId())
                .companyName(industrialAccident.getCompany().getCompanyName())
                .jobName(industrialAccident.getJobCode().getJobName())
                .industrialAccidentCode(industrialAccident.getIndustrialAccidentCode())
                .industrialAccidentName(industrialAccident.getIndustrialAccidentName())
                .industrialAccidentDate(industrialAccident.getIndustrialAccidentDate())
                .message(message)
                .build();
    }
} 