package com.example.workerManagers.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final TokenBlacklist tokenBlacklist;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        logger.debug("요청 URI: {}, 메서드: {}", requestURI, method);
        
        // 로그아웃 테스트 요청인 경우 특별 처리
        if (requestURI.equals("/users/logout-test") && method.equals("GET")) {
            logger.info("로그아웃 테스트 요청이 감지되었습니다. URI: {}, 메서드: {}", requestURI, method);
            // 로그아웃 테스트 요청은 인증 없이 처리
            filterChain.doFilter(request, response);
            return;
        }
        
        // 로그아웃 요청인 경우 토큰 검증 후 처리
        if (requestURI.equals("/users/logout") && method.equals("POST")) {
            logger.info("로그아웃 요청이 감지되었습니다. URI: {}, 메서드: {}", requestURI, method);
            
            String token = resolveToken(request);
            if (token == null) {
                // 토큰이 없는 경우 401 응답
                logger.warn("로그아웃 요청에 토큰이 없습니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\": \"인증 토큰이 필요합니다.\", \"success\": false}");
                return;
            }
            
            if (tokenProvider.validateToken(token) && !tokenBlacklist.isBlacklisted(token)) {
                // 토큰이 유효한 경우에만 로그아웃 처리
                logger.info("로그아웃 요청의 토큰이 유효합니다.");
                filterChain.doFilter(request, response);
            } else {
                // 토큰이 유효하지 않은 경우 401 응답
                logger.warn("로그아웃 요청의 토큰이 유효하지 않습니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\": \"유효하지 않은 토큰입니다.\", \"success\": false}");
                return;
            }
            return;
        }
        
        String token = resolveToken(request);
        
        if (token != null) {
            logger.debug("요청에서 토큰을 추출했습니다: {}", token);
            
            // 토큰이 유효하고 블랙리스트에 없는 경우에만 인증 처리
            if (tokenProvider.validateToken(token) && !tokenBlacklist.isBlacklisted(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("토큰이 유효하며 인증 정보가 설정되었습니다.");
            } else {
                // 토큰이 유효하지 않거나 블랙리스트에 있는 경우 인증 정보 초기화
                SecurityContextHolder.clearContext();
                logger.warn("토큰이 유효하지 않거나 블랙리스트에 있습니다.");
            }
        } else {
            logger.debug("요청에 토큰이 없습니다.");
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 