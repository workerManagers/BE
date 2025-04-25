package com.example.workerManagers.domain.users.service;

import com.example.workerManagers.domain.users.dto.LoginRequestDto;
import com.example.workerManagers.domain.users.dto.LoginResponseDto;
import com.example.workerManagers.domain.users.dto.SignupRequestDto;
import com.example.workerManagers.domain.users.dto.SignupResponseDto;
import com.example.workerManagers.domain.users.dto.UserResponseDto;

public interface UserService {
    SignupResponseDto signup(SignupRequestDto requestDto);
    LoginResponseDto login(LoginRequestDto requestDto);
    void logout(String token);
    UserResponseDto getUserInfo(String userEmail);
} 