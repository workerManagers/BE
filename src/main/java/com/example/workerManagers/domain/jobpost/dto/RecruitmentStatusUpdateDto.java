package com.example.workerManagers.domain.jobpost.dto;

import com.example.workerManagers.domain.jobpost.entity.RecruitmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RecruitmentStatusUpdateDto {
    @NotNull(message = "고용상태는 필수입니다.")
    private RecruitmentStatus status;
} 