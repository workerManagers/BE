package com.example.workerManagers.domain.aimatching.entity;

import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ai_matching")
public class AIMatching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long matchingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code")
    private JobCode jobCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id")
    private JobPost jobPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "matching_score")
    private Double matchingScore;

    @OneToMany(mappedBy = "aiMatching", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications = new HashSet<>();

    @OneToMany(mappedBy = "aiMatching", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resume> resumes = new HashSet<>();

    @Builder
    public AIMatching(Company company, JobCode jobCode, JobPost jobPost, User user, Double matchingScore) {
        this.company = company;
        this.jobCode = jobCode;
        this.jobPost = jobPost;
        this.user = user;
        this.matchingScore = matchingScore;
        this.applications = new HashSet<>();
        this.resumes = new HashSet<>();
    }

    public void update(Company company, JobCode jobCode, JobPost jobPost, User user, Double matchingScore) {
        this.company = company;
        this.jobCode = jobCode;
        this.jobPost = jobPost;
        this.user = user;
        this.matchingScore = matchingScore;
    }
}