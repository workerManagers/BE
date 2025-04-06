package com.example.workerManagers.domain.users.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponseDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private String message;
} 