package com.example.workerManagers.domain.jobpost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostRequestDto {
    @NotBlank(message = "회사 이름은 필수입니다.")
    @Size(max = 100, message = "회사 이름은 최대 100자까지 입력 가능합니다.")
    private String companyName;

    @NotBlank(message = "직종 이름은 필수입니다.")
    @Size(max = 100, message = "직종 이름은 최대 100자까지 입력 가능합니다.")
    private String jobName;

    @NotBlank(message = "공고 설명은 필수입니다.")
    @Size(max = 500, message = "공고 설명은 최대 500자까지 입력 가능합니다.")
    private String jobPostDescription;

    @NotBlank(message = "근무 기간은 필수입니다.")
    @Size(max = 50, message = "근무 기간은 최대 50자까지 입력 가능합니다.")
    private String jobPeriod;

    @NotBlank(message = "근무 지역은 필수입니다.")
    @Size(max = 50, message = "근무 지역은 최대 50자까지 입력 가능합니다.")
    private String jobRegion;

    @NotNull(message = "마감일은 필수입니다.")
    private LocalDateTime deadline;
} 