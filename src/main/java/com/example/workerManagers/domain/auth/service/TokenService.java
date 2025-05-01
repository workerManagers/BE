package com.example.workerManagers.domain.auth.service;

import com.example.workerManagers.domain.auth.dto.TokenDto;
import com.example.workerManagers.domain.auth.entity.RefreshToken;
import com.example.workerManagers.domain.auth.repository.RefreshTokenRepository;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.global.security.JwtTokenProvider;
import com.example.workerManagers.global.security.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklist tokenBlacklist;

    @Transactional
    public TokenDto createTokenDto(String userEmail, User.UserType userType) {
        // Access Token 생성
        String accessToken = tokenProvider.createAccessToken(userEmail, userType);
        
        // Refresh Token 생성
        Date now = new Date();
        Date refreshTokenExpiry = new Date(now.getTime() + (14 * 24 * 60 * 60 * 1000)); // 14일
        String refreshToken = tokenProvider.createRefreshToken(userEmail, userType);

        // RefreshToken 저장
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .userEmail(userEmail)
                .expiryDate(refreshTokenExpiry.toInstant())
                .build();

        refreshTokenRepository.findByUserEmail(userEmail)
                .ifPresent(token -> refreshTokenRepository.delete(token));
        
        refreshTokenRepository.save(refreshTokenEntity);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(tokenProvider.getExpirationDateFromToken(accessToken).getTime())
                .build();
    }

    @Transactional
    public TokenDto refreshToken(String refreshToken) {
        // Refresh Token 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        // Refresh Token으로부터 사용자 정보 추출
        String userEmail = tokenProvider.getUserEmailFromToken(refreshToken);
        User.UserType userType = tokenProvider.getUserTypeFromToken(refreshToken);

        // DB에서 Refresh Token 확인
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("데이터베이스에서 리프레시 토큰을 찾을 수 없습니다."));

        if (refreshTokenEntity.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new RuntimeException("리프레시 토큰이 만료되었습니다.");
        }

        // 새로운 토큰 생성
        return createTokenDto(userEmail, userType);
    }

    @Transactional
    public TokenDto extendSession(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new RuntimeException("유효하지 않은 액세스 토큰입니다.");
        }

        String userEmail = tokenProvider.getUserEmailFromToken(accessToken);
        User.UserType userType = tokenProvider.getUserTypeFromToken(accessToken);

        // 기존 토큰 블랙리스트에 추가
        tokenBlacklist.addToBlacklist(accessToken);

        // 새로운 액세스 토큰만 생성
        String newAccessToken = tokenProvider.createAccessToken(userEmail, userType);
        
        return TokenDto.builder()
                .accessToken(newAccessToken)
                .accessTokenExpiresIn(tokenProvider.getExpirationDateFromToken(newAccessToken).getTime())
                .build();
    }
} 