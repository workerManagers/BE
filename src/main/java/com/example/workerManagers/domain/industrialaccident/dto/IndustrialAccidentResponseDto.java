package com.example.workerManagers.domain.industrialaccident.dto;

import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IndustrialAccidentResponseDto {
    private Long industrialAccidentId;
    private Long companyId;
    private Long jobCodeId;
    private String industrialAccidentCode;
    private String industrialAccidentName;
    private LocalDateTime industrialAccidentDate;
    private String message;

    public static IndustrialAccidentResponseDto of(IndustrialAccident industrialAccident, String message) {
        return IndustrialAccidentResponseDto.builder()
                .industrialAccidentId(industrialAccident.getIndustrialAccidentId())
                .companyId(industrialAccident.getCompany().getCompanyId())
                .jobCodeId(industrialAccident.getJobCode().getJobCodeId())
                .industrialAccidentCode(industrialAccident.getIndustrialAccidentCode())
                .industrialAccidentName(industrialAccident.getIndustrialAccidentName())
                .industrialAccidentDate(industrialAccident.getIndustrialAccidentDate())
                .message(message)
                .build();
    }
} 