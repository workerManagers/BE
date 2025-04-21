package com.example.workerManagers.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final UserDetailsService userDetailsService;
    private final long tokenValidityInMilliseconds;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public JwtTokenProvider(
            @Value("${jwt.secret:your-secret-key}") String secret,
            @Value("${jwt.token-validity-in-seconds:3600}") long tokenValidityInSeconds,
            UserDetailsService userDetailsService) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String email) {
        logger.info("토큰 생성 시작: {}", email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
        
        logger.info("토큰 생성 완료: {}", email);
        return token;
    }

    public Authentication getAuthentication(String token) {
        logger.info("인증 정보 추출 시작");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        logger.info("인증 정보 추출 완료: {}", claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        logger.info("토큰 유효성 검사 시작");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            logger.info("토큰 유효성 검사 성공");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("토큰 유효성 검사 실패: {}", e.getMessage());
            return false;
        }
    }
}