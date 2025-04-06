package com.example.workerManagers.domain.users.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogoutResponseDto {
    private String message;
    private String error;
    private boolean success;
} 