package com.example.workerManagers.domain.industrialaccident.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IndustrialAccidentRequestDto {
    @NotNull(message = "회사 ID는 필수 입력값입니다.")
    private Long companyId;
    
    @NotNull(message = "직종 코드 ID는 필수 입력값입니다.")
    private Long jobCodeId;
    
    @NotBlank(message = "산업재해 코드는 필수 입력값입니다.")
    @Size(max = 20, message = "산업재해 코드는 20자를 초과할 수 없습니다.")
    private String industrialAccidentCode;
    
    @NotBlank(message = "산업재해 이름은 필수 입력값입니다.")
    private String industrialAccidentName;
    
    @NotNull(message = "산업재해 발생일은 필수 입력값입니다.")
    private LocalDateTime industrialAccidentDate;
} 