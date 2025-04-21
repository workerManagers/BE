package com.example.workerManagers.domain.jobpost.entity;

import com.example.workerManagers.domain.aimatching.entity.AIMatching;
import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "job_post")
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_post_id")
    private Long jobPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code")
    private JobCode jobCode;

    @Column(name = "job_post_description", nullable = false, length = 500)
    private String jobPostDescription;

    @Column(name = "job_period", nullable = false, length = 50)
    private String jobPeriod;

    @Column(name = "job_region", nullable = false, length = 50)
    private String jobRegion;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AIMatching> aiMatchings;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resume> resumes;

    @Builder
    public JobPost(Company company, JobCode jobCode, String jobPostDescription, String jobPeriod, String jobRegion, LocalDateTime deadline) {
        this.company = company;
        this.jobCode = jobCode;
        this.jobPostDescription = jobPostDescription;
        this.jobPeriod = jobPeriod;
        this.jobRegion = jobRegion;
        this.deadline = deadline;
        this.applications = new HashSet<>();
        this.aiMatchings = new HashSet<>();
        this.resumes = new HashSet<>();
    }

    public void update(Company company, JobCode jobCode, String jobPostDescription, String jobPeriod, String jobRegion, LocalDateTime deadline) {
        this.company = company;
        this.jobCode = jobCode;
        this.jobPostDescription = jobPostDescription;
        this.jobPeriod = jobPeriod;
        this.jobRegion = jobRegion;
        this.deadline = deadline;
    }
}