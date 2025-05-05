package com.example.workerManagers.domain.users.controller;

import com.example.workerManagers.domain.users.dto.LoginRequestDto;
import com.example.workerManagers.domain.users.dto.LoginResponseDto;
import com.example.workerManagers.domain.users.dto.SignupRequestDto;
import com.example.workerManagers.domain.users.dto.SignupResponseDto;
import com.example.workerManagers.domain.users.dto.LogoutResponseDto;
import com.example.workerManagers.domain.users.dto.UserResponseDto;
import com.example.workerManagers.domain.users.dto.MatchingStatusUpdateDto;
import com.example.workerManagers.domain.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUserInfo(Authentication authentication) {
        log.info("사용자 정보 조회 요청 수신");
        String userEmail = authentication.getName();
        UserResponseDto responseDto = userService.getUserInfo(userEmail);
        log.info("사용자 정보 조회 완료: {}", userEmail);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/name/{userName}")
    public ResponseEntity<UserResponseDto> getUserByName(@PathVariable String userName) {
        log.info("사용자 이름으로 조회 요청: {}", userName);
        UserResponseDto userResponseDto = userService.getUserByName(userName);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        log.info("사용자 ID로 조회 요청: {}", userId);
        UserResponseDto userResponseDto = userService.getUserById(userId);
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping("/matching-status")
    public ResponseEntity<?> updateMatchingStatus(
            @RequestBody MatchingStatusUpdateDto requestDto,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            UserResponseDto responseDto = userService.updateMatchingStatus(userEmail, requestDto.getMatchingEnabled());
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
} 