package com.example.workerManagers.domain.users.dto;

import com.example.workerManagers.domain.users.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long userId;
    private String userName;
    private User.UserType userType;
} 