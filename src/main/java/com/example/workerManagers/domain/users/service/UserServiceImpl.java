package com.example.workerManagers.domain.users.service;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.domain.users.dto.LoginRequestDto;
import com.example.workerManagers.domain.users.dto.LoginResponseDto;
import com.example.workerManagers.domain.users.dto.SignupRequestDto;
import com.example.workerManagers.domain.users.dto.SignupResponseDto;
import com.example.workerManagers.domain.users.dto.UserResponseDto;
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
import java.util.HashSet;

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

        // 사용자 저장을 먼저 수행
        User savedUser = userRepository.save(user);
        log.info("사용자 정보 저장 완료: {}", savedUser.getUserEmail());

        // 기업 회원인 경우 기업 정보 저장
        if (requestDto.getUserType() == User.UserType.COMPANY && requestDto.getCompanyInfo() != null) {
            SignupRequestDto.CompanyInfo companyInfo = requestDto.getCompanyInfo();
            
            // 기업 정보 생성
            Company company = Company.builder()
                    .companyName(companyInfo.getCompanyName())
                    .companyRegion(companyInfo.getCompanyRegion())
                    .companyCode(companyInfo.getCompanyCode())
                    .industrialAccidents(new HashSet<>())
                    .restPeriods(new HashSet<>())
                    .jobPosts(new HashSet<>())
                    .applications(new HashSet<>())
                    .user(savedUser)  // 저장된 User 엔티티 설정
                    .build();
            
            // 기업 정보 저장
            Company savedCompany = companyRepository.save(company);
            log.info("기업 정보 저장 완료: {}", savedCompany.getCompanyName());
            
            // 저장된 Company 엔티티로 User 업데이트
            savedUser.setCompany(savedCompany);
            userRepository.save(savedUser);
        }

        return SignupResponseDto.builder()
                .userId(savedUser.getUserId())
                .userName(savedUser.getUserName())
                .userEmail(savedUser.getUserEmail())
                .userType(savedUser.getUserType())
                .message("회원가입이 성공적으로 완료되었습니다.")
                .build();
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        log.info("로그인 시도: {}", requestDto.getUserEmail());
        try {
            // 사용자 존재 여부 확인
            User user = userRepository.findByUserEmail(requestDto.getUserEmail())
                    .orElseThrow(() -> {
                        log.error("존재하지 않는 사용자: {}", requestDto.getUserEmail());
                        return new UserException("이메일 또는 비밀번호가 일치하지 않습니다.");
                    });

            // 인증 처리
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(requestDto.getUserEmail(), requestDto.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                log.error("인증 실패: {} - {}", requestDto.getUserEmail(), e.getMessage());
                throw new UserException("이메일 또는 비밀번호가 일치하지 않습니다.");
            }
            
            // JWT 토큰 생성
            String token = tokenProvider.createToken(requestDto.getUserEmail(), user.getUserType());
            
            log.info("로그인 성공: {}", requestDto.getUserEmail());
            
            return LoginResponseDto.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .userType(user.getUserType())
                    .build();
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: {} - {}", requestDto.getUserEmail(), e.getMessage());
            throw new UserException("로그인 처리 중 오류가 발생했습니다.");
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

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(String userEmail) {
        log.info("사용자 정보 조회 시작: {}", userEmail);
        
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 사용자: {}", userEmail);
                    return new UserException("사용자를 찾을 수 없습니다.");
                });

        UserResponseDto.UserResponseDtoBuilder builder = UserResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userSex(user.getUserSex())
                .userAge(user.getUserAge())
                .userType(user.getUserType());

        // 기업 회원인 경우 기업 정보도 포함
        if (user.getUserType() == User.UserType.COMPANY) {
            Company company = companyRepository.findByUser(user)
                    .orElse(null);
            
            if (company != null) {
                UserResponseDto.CompanyInfo companyInfo = UserResponseDto.CompanyInfo.builder()
                        .companyName(company.getCompanyName())
                        .companyRegion(company.getCompanyRegion())
                        .companyCode(company.getCompanyCode())
                        .build();
                
                builder.companyInfo(companyInfo);
            }
        }

        UserResponseDto responseDto = builder.build();
        log.info("사용자 정보 조회 완료: {}", userEmail);
        return responseDto;
    }
} 