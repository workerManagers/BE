package com.example.workerManagers.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {
    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
} 