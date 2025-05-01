package com.example.workerManagers.domain.users.repository;

import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserEmail(String email);
    Optional<User> findByUserEmail(String email);
    Optional<User> findByUserName(String userName);
} 