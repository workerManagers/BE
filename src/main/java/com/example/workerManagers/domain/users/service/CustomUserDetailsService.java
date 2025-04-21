package com.example.workerManagers.domain.users.service;

import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("사용자 정보 로드 시작: {}", email);
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다: {}", email);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
                });

        log.info("사용자 정보 로드 완료: {}", email);
        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
} 