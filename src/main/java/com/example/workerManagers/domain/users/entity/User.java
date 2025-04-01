package com.example.workerManagers.domain.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user") // 데이터베이스 테이블 이름
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 증가 설정
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_pass", length = 20, nullable = false)
    private String userPass;

    @Column(name = "user_name", length = 20, nullable = false)
    private String userName;

    @Column(name = "user_sex", length = 20, nullable = false)
    private String userSex;

    @Column(name = "user_age", nullable = false)
    private Integer userAge;

    @Column(name = "user_email", length = 20, nullable = false)
    private String userEmail;

    // Getters and Setters (생략 가능)
}