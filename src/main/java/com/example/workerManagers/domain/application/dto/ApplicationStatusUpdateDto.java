package com.example.workerManagers.domain.application.dto;

import com.example.workerManagers.domain.application.entity.ApplicationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationStatusUpdateDto {
    private ApplicationStatus status;
} 