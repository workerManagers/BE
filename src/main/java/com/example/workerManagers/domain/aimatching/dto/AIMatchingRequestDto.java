package com.example.workerManagers.domain.aimatching.dto;

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
public class AIMatchingRequestDto {
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

    @NotNull(message = "매칭 점수는 필수입니다.")
    private Double matchingScore;
} 