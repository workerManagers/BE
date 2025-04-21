package com.example.workerManagers.domain.users.service;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.domain.users.dto.LoginRequestDto;
import com.example.workerManagers.domain.users.dto.LoginResponseDto;
import com.example.workerManagers.domain.users.dto.SignupRequestDto;
import com.example.workerManagers.domain.users.dto.SignupResponseDto;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.exception.UserException;
import com.example.workerManagers.domain.users.repository.UserRepository;
import com.example.workerManagers.global.security.JwtTokenProvider;
import com.example.workerManagers.global.security.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklist tokenBlacklist;

    @Override
    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        log.info("회원가입 요청: {}", requestDto.getUserEmail());
        
        // 이메일 중복 검사
        if (userRepository.existsByUserEmail(requestDto.getUserEmail())) {
            log.warn("이메일 중복: {}", requestDto.getUserEmail());
            throw new UserException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 사용자 생성
        User user = User.builder()
                .userName(requestDto.getUserName())
                .password(encodedPassword)
                .userSex(requestDto.getUserSex())
                .userAge(requestDto.getUserAge())
                .userEmail(requestDto.getUserEmail())
                .userType(requestDto.getUserType())
                .build();

        // 기업 회원인 경우 기업 정보 저장
        if (requestDto.getCompanyId() != null) {
            Company company = companyRepository.findById(requestDto.getCompanyId())
                    .orElseThrow(() -> new UserException("존재하지 않는 기업입니다."));
            user.setCompany(company);
        }

        // 사용자 저장
        User savedUser = userRepository.save(user);
        log.info("회원가입 성공: {}", savedUser.getUserEmail());

        return SignupResponseDto.builder()
                .userId(savedUser.getUserId())
                .userName(savedUser.getUserName())
                .userEmail(savedUser.getUserEmail())
                .build();
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        log.info("로그인 시도: {}", requestDto.getUserEmail());
        try {
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
            
            log.info("로그인 성공: {}", requestDto.getUserEmail());
            
            return LoginResponseDto.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .build();
        } catch (Exception e) {
            log.error("로그인 실패: {} - {}", requestDto.getUserEmail(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void logout(String token) {
        log.info("로그아웃 처리 시작");
        
        if (token == null || token.isEmpty()) {
            log.warn("로그아웃 시도: 토큰이 없습니다.");
            throw new RuntimeException("인증 토큰이 필요합니다.");
        }
        
        if (!tokenProvider.validateToken(token)) {
            log.warn("로그아웃 시도: 유효하지 않은 토큰입니다.");
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
        
        if (tokenBlacklist.isBlacklisted(token)) {
            log.warn("로그아웃 시도: 이미 로그아웃된 토큰입니다.");
            throw new RuntimeException("이미 로그아웃된 토큰입니다.");
        }
        
        // 토큰을 블랙리스트에 추가
        tokenBlacklist.addToBlacklist(token);
        log.info("토큰이 블랙리스트에 추가되었습니다.");
        
        // SecurityContext 초기화
        SecurityContextHolder.clearContext();
        log.info("SecurityContext가 초기화되었습니다.");
        
        log.info("로그아웃 처리 완료");
    }
} 