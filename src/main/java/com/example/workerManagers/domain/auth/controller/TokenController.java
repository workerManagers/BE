package com.example.workerManagers.domain.auth.controller;

import com.example.workerManagers.domain.auth.dto.TokenDto;
import com.example.workerManagers.domain.auth.dto.TokenResponseDto;
import com.example.workerManagers.domain.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        try {
            TokenDto tokenDto = tokenService.refreshToken(refreshToken);
            return ResponseEntity.ok(TokenResponseDto.builder()
                    .success(true)
                    .message("토큰이 성공적으로 갱신되었습니다.")
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getRefreshToken())
                    .accessTokenExpiresIn(tokenDto.getAccessTokenExpiresIn())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TokenResponseDto.builder()
                    .success(false)
                    .message("토큰 갱신에 실패했습니다: " + e.getMessage())
                    .build());
        }
    }

    @PostMapping("/extend")
    public ResponseEntity<TokenResponseDto> extendSession(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.substring(7);
            TokenDto tokenDto = tokenService.extendSession(token);
            return ResponseEntity.ok(TokenResponseDto.builder()
                    .success(true)
                    .message("세션이 성공적으로 연장되었습니다.")
                    .accessToken(tokenDto.getAccessToken())
                    .accessTokenExpiresIn(tokenDto.getAccessTokenExpiresIn())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TokenResponseDto.builder()
                    .success(false)
                    .message("세션 연장에 실패했습니다: " + e.getMessage())
                    .build());
        }
    }
} 