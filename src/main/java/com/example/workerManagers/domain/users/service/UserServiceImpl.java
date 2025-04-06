package com.example.workerManagers.domain.users.service;

import com.example.workerManagers.domain.users.dto.LoginRequestDto;
import com.example.workerManagers.domain.users.dto.LoginResponseDto;
import com.example.workerManagers.domain.users.dto.SignupRequestDto;
import com.example.workerManagers.domain.users.dto.SignupResponseDto;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.exception.UserException;
import com.example.workerManagers.domain.users.repository.UserRepository;
import com.example.workerManagers.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        // 이메일 중복 검사
        if (userRepository.existsByUserEmail(requestDto.getUserEmail())) {
            throw new UserException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        User user = User.builder()
                .userName(requestDto.getUserName())
                .password(requestDto.getPassword())
                .userSex(requestDto.getUserSex())
                .userAge(requestDto.getUserAge())
                .userEmail(requestDto.getUserEmail())
                .build();

        User savedUser = userRepository.save(user);

        return SignupResponseDto.builder()
                .userId(savedUser.getUserId())
                .userName(savedUser.getUserName())
                .userEmail(savedUser.getUserEmail())
                .message("회원가입이 완료되었습니다.")
                .build();
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUserEmail(), requestDto.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // JWT 토큰 생성
        String token = tokenProvider.createToken(requestDto.getUserEmail());
        
        // 사용자 정보 조회
        User user = userRepository.findByUserEmail(requestDto.getUserEmail())
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));
        
        return LoginResponseDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .userName(user.getUserName())
                .build();
    }
} 