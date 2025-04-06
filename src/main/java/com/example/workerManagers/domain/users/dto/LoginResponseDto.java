package com.example.workerManagers.domain.users.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String tokenType;
    private Long userId;
    private String userName;
} 