package com.example.workerManagers.domain.resume.entity;

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
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long resumeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_id")
    private AIMatching aiMatching;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPost_id", nullable = false)
    private JobPost jobPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industrialAccident_id", nullable = false)
    private IndustrialAccident industrialAccident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code", nullable = false)
    private JobCode jobCode;

    @Column(name = "resume_area", length = 20)
    private String resumeArea;

    @Column(name = "resume_history", length = 100)
    private String resumeHistory;
}