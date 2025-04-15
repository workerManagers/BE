package com.example.workerManagers.domain.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRequestDto {

    @NotNull(message = "회사 ID는 필수 입력값입니다.")
    private Long companyId;

    @NotNull(message = "산업재해 ID는 필수 입력값입니다.")
    private Long industrialAccidentId;

    @NotNull(message = "직종 코드 ID는 필수 입력값입니다.")
    private Long jobCodeId;

    @NotNull(message = "모집공고 ID는 필수 입력값입니다.")
    private Long jobPostId;

    @NotNull(message = "사용자 ID는 필수 입력값입니다.")
    private Long userId;

    @NotNull(message = "직무 설명은 필수 입력값입니다.")
    private String jobDescription;

    @NotNull(message = "근무 기간은 필수 입력값입니다.")
    private String jobPeriod;

    @NotNull(message = "지원 결과는 필수 입력값입니다.")
    private String applyResult;
} 