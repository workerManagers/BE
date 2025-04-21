package com.example.workerManagers.domain.users.controller;

import com.example.workerManagers.domain.users.dto.LoginRequestDto;
import com.example.workerManagers.domain.users.dto.LoginResponseDto;
import com.example.workerManagers.domain.users.dto.SignupRequestDto;
import com.example.workerManagers.domain.users.dto.SignupResponseDto;
import com.example.workerManagers.domain.users.dto.LogoutResponseDto;
import com.example.workerManagers.domain.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        log.info("회원가입 요청 수신: {}", requestDto.getUserEmail());
        SignupResponseDto responseDto = userService.signup(requestDto);
        log.info("회원가입 성공: {}", requestDto.getUserEmail());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        log.info("로그인 요청 수신: {}", requestDto.getUserEmail());
        try {
            LoginResponseDto responseDto = userService.login(requestDto);
            log.info("로그인 성공: {}", requestDto.getUserEmail());
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("로그인 실패: {} - {}", requestDto.getUserEmail(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout() {
        log.info("로그아웃 요청이 들어왔습니다.");
        
        try {
            // 현재 요청에서 토큰 추출
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = extractToken(request);
                
                if (token != null) {
                    try {
                        // 토큰이 유효한 경우에만 로그아웃 처리
                        userService.logout(token);
                        log.info("로그아웃이 성공적으로 처리되었습니다.");
                        return ResponseEntity.ok()
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .body(LogoutResponseDto.builder()
                                        .message("로그아웃이 완료되었습니다.")
                                        .success(true)
                                        .build());
                    } catch (RuntimeException e) {
                        // UserServiceImpl에서 던진 예외 처리
                        log.error("로그아웃 처리 중 오류가 발생했습니다: {}", e.getMessage(), e);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .body(LogoutResponseDto.builder()
                                        .error(e.getMessage())
                                        .success(false)
                                        .build());
                    }
                } else {
                    // 토큰이 없는 경우
                    log.warn("로그아웃 시도: 토큰이 없습니다.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                            .body(LogoutResponseDto.builder()
                                    .error("인증 토큰이 필요합니다.")
                                    .success(false)
                                    .build());
                }
            } else {
                // 요청 정보를 가져올 수 없는 경우
                log.warn("로그아웃 시도: RequestContextHolder에서 요청 정보를 가져올 수 없습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .body(LogoutResponseDto.builder()
                                .error("요청 정보를 가져올 수 없습니다.")
                                .success(false)
                                .build());
            }
        } catch (Exception e) {
            log.error("로그아웃 처리 중 오류가 발생했습니다: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(LogoutResponseDto.builder()
                            .error("로그아웃 처리 중 오류가 발생했습니다.")
                            .success(false)
                            .build());
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 