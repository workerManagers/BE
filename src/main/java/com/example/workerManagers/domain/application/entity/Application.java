package com.example.workerManagers.domain.application.entity;

import com.example.workerManagers.domain.aimatching.entity.AIMatching;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industrialAccident_id", nullable = false)
    private IndustrialAccident industrialAccident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code_id", nullable = false)
    private JobCode jobCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPost_id", nullable = false)
    private JobPost jobPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_id")
    private AIMatching aiMatching;

    @Column(name = "job_description", length = 100)
    private String jobDescription;

    @Column(name = "job_period")
    private String jobPeriod;

    @Column(name = "apply_result")
    private String applyResult;
}