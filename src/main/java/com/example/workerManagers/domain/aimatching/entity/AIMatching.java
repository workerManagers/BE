package com.example.workerManagers.domain.aimatching.entity;

import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.resume.entity.Resume;
import com.example.workerManagers.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIMatching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long matchingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industrialAccident_id", nullable = false)
    private IndustrialAccident industrialAccident;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code", nullable = false)
    private JobCode jobCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPost_id", nullable = false)
    private JobPost jobPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "matching_score")
    private Double matchingScore;

    @OneToMany(mappedBy = "aiMatching", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;

    @OneToMany(mappedBy = "aiMatching", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resume> resumes;
}