package com.example.workerManagers.domain.jobpost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobPostRequestDto {
    @NotNull(message = "산업재해 ID는 필수 입력값입니다.")
    private Long industrialAccidentId;
    
    @NotNull(message = "회사 ID는 필수 입력값입니다.")
    private Long companyId;
    
    @NotNull(message = "직종 코드 ID는 필수 입력값입니다.")
    private Long jobCodeId;
    
    @NotBlank(message = "모집공고 설명은 필수 입력값입니다.")
    @Size(max = 100, message = "모집공고 설명은 100자를 초과할 수 없습니다.")
    private String jobPostDescription;
    
    @NotBlank(message = "근무 기간은 필수 입력값입니다.")
    private String jobPeriod;
    
    @NotNull(message = "마감일은 필수 입력값입니다.")
    private LocalDateTime deadline;
} 