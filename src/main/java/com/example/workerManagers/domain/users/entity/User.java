package com.example.workerManagers.domain.users.entity;

import com.example.workerManagers.domain.aimatching.entity.AIMatching;
import com.example.workerManagers.domain.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "user") // 데이터베이스 테이블 이름
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 증가 설정
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_name", length = 20, nullable = false)
    private String userName;

    @Column(name = "user_sex", length = 20, nullable = false)
    private String userSex;

    @Column(name = "user_age", nullable = false)
    private Integer userAge;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<AIMatching> aiMatchings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Resume> resumes;
}