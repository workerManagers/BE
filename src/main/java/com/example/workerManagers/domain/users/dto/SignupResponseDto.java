package com.example.workerManagers.domain.users.dto;

import com.example.workerManagers.domain.users.entity.User.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponseDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private UserType userType;
    private String message;
} 