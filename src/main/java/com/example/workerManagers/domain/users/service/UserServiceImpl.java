package com.example.workerManagers.domain.users.service;

import com.example.workerManagers.domain.users.dto.UserRequestDto;
import com.example.workerManagers.domain.users.dto.UserResponseDto;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.exception.UserException;
import com.example.workerManagers.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto signup(UserRequestDto requestDto) {
        // 이메일 중복 검사
        if (userRepository.existsByUserEmail(requestDto.getUserEmail())) {
            throw new UserException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        User user = User.builder()
                .userName(requestDto.getUserName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .userSex(requestDto.getUserSex())
                .userAge(requestDto.getUserAge())
                .userEmail(requestDto.getUserEmail())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .userId(savedUser.getUserId())
                .userName(savedUser.getUserName())
                .userEmail(savedUser.getUserEmail())
                .message("회원가입이 완료되었습니다.")
                .build();
    }
} 