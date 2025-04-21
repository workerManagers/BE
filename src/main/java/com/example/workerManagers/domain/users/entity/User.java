package com.example.workerManagers.domain.users.entity;

import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @OneToOne(mappedBy = "user")
    private Company company;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Application> applications = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Resume> resumes = new HashSet<>();

    public enum UserType {
        INDIVIDUAL,  // 일반 회원
        COMPANY     // 기업 회원
    }
}