package com.example.workerManagers.domain.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationRequestDto {
    @NotNull(message = "채용 공고 ID는 필수입니다.")
    private Long jobPostId;
} 