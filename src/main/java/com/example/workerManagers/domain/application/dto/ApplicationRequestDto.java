package com.example.workerManagers.domain.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequestDto {
    @NotBlank(message = "회사 이름은 필수입니다.")
    @Size(max = 100, message = "회사 이름은 최대 100자까지 입력 가능합니다.")
    private String companyName;

    @NotBlank(message = "직종 이름은 필수입니다.")
    @Size(max = 100, message = "직종 이름은 최대 100자까지 입력 가능합니다.")
    private String jobName;

    @NotNull(message = "채용 공고 ID는 필수입니다.")
    private Long jobPostId;

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "직무 설명은 필수입니다.")
    @Size(max = 100, message = "직무 설명은 최대 100자까지 입력 가능합니다.")
    private String jobDescription;

    @NotBlank(message = "근무 기간은 필수입니다.")
    @Size(max = 50, message = "근무 기간은 최대 50자까지 입력 가능합니다.")
    private String jobPeriod;

    @NotBlank(message = "지원 결과는 필수입니다.")
    private String applyResult;
} 