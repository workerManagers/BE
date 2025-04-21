package com.example.workerManagers.domain.users.dto;

import com.example.workerManagers.domain.users.entity.User.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponseDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private UserType userType;
    private String message;
} 