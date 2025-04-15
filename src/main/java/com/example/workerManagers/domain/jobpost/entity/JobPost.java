package com.example.workerManagers.domain.jobpost.entity;

import com.example.workerManagers.domain.aimatching.entity.AIMatching;
import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobPost_id")
    private Long jobPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industrialAccident_id", nullable = false)
    private IndustrialAccident industrialAccident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code", nullable = false)
    private JobCode jobCode;

    @Column(name = "jobPost_description")
    private String jobPostDescription;

    @Column(name = "job_period")
    private String jobPeriod;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AIMatching> aiMatchings;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resume> resumes;

    public void update(Company company, IndustrialAccident industrialAccident, JobCode jobCode,
                     String jobPostDescription, String jobPeriod, LocalDateTime deadline) {
        this.company = company;
        this.industrialAccident = industrialAccident;
        this.jobCode = jobCode;
        this.jobPostDescription = jobPostDescription;
        this.jobPeriod = jobPeriod;
        this.deadline = deadline;
    }
}