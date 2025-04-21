// src/main/java/com/example/workerManagers/domain/restperiod/controller/RestPeriodController.java
package com.example.workerManagers.domain.restperiod.controller;

import com.example.workerManagers.domain.restperiod.dto.RestPeriodRequestDto;
import com.example.workerManagers.domain.restperiod.dto.RestPeriodResponseDto;
import com.example.workerManagers.domain.restperiod.service.RestPeriodService;
import com.example.workerManagers.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/predict")
@RequiredArgsConstructor
public class RestPeriodController {
    
    private static final Logger logger = LoggerFactory.getLogger(RestPeriodController.class);
    private final RestPeriodService restPeriodService;
    private final JwtTokenProvider tokenProvider;
    
    @PostMapping
    public ResponseEntity<RestPeriodResponseDto> predictRestPeriod(
            @Valid @RequestBody RestPeriodRequestDto request,
            HttpServletRequest httpRequest) {
        logger.info("요양기간 예측 요청 수신 - URI: {}, Method: {}, RemoteAddr: {}", 
                httpRequest.getRequestURI(), 
                httpRequest.getMethod(), 
                httpRequest.getRemoteAddr());
        logger.info("요청 데이터: {}", request);
        
        try {
            // JWT 토큰에서 사용자 이메일 추출
            String token = extractToken(httpRequest);
            logger.info("추출된 토큰: {}", token);
            
            if (token == null) {
                logger.error("토큰이 없습니다.");
                throw new RuntimeException("인증 토큰이 필요합니다.");
            }
            
            String userEmail = tokenProvider.getUserEmailFromToken(token);
            logger.info("토큰에서 추출한 사용자 이메일: {}", userEmail);
            
            RestPeriodResponseDto response = restPeriodService.predictRestPeriod(request, userEmail);
            logger.info("요양기간 예측 응답: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("요양기간 예측 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.info("Authorization 헤더: {}", bearerToken);
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            logger.info("Bearer 토큰 추출 성공");
            return token;
        }
        logger.warn("Bearer 토큰 추출 실패");
        return null;
    }
}