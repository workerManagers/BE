package com.example.workerManagers.domain.users.service;

import com.example.workerManagers.domain.users.dto.UserRequestDto;
import com.example.workerManagers.domain.users.dto.UserResponseDto;

public interface UserService {
    UserResponseDto signup(UserRequestDto requestDto);
} 