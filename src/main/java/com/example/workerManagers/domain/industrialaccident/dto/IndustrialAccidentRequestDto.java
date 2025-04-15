package com.example.workerManagers.domain.industrialaccident.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IndustrialAccidentRequestDto {
    @NotBlank(message = "회사명은 필수 입력값입니다.")
    @Size(max = 100, message = "회사명은 최대 100자까지 입력 가능합니다.")
    private String companyName;

    @NotBlank(message = "직종명은 필수 입력값입니다.")
    @Size(max = 100, message = "직종명은 최대 100자까지 입력 가능합니다.")
    private String jobName;

    @NotBlank(message = "산업재해 코드는 필수 입력값입니다.")
    @Size(max = 20, message = "산업재해 코드는 최대 20자까지 입력 가능합니다.")
    private String industrialAccidentCode;

    @NotBlank(message = "산업재해명은 필수 입력값입니다.")
    @Size(max = 100, message = "산업재해명은 최대 100자까지 입력 가능합니다.")
    private String industrialAccidentName;

    @NotNull(message = "산업재해 발생일은 필수 입력값입니다.")
    private LocalDateTime industrialAccidentDate;
} 