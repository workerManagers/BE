package com.example.workerManagers.domain.auth.repository;

import com.example.workerManagers.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserEmail(String userEmail);
    void deleteByUserEmail(String userEmail);
    boolean existsByUserEmail(String userEmail);
} 