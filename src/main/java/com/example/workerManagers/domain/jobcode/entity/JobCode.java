package com.example.workerManagers.domain.jobcode.entity;

import com.example.workerManagers.domain.aimatching.entity.AIMatching;
import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.restperiod.entity.RestPeriod;
import com.example.workerManagers.domain.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_code_id")
    private Long jobCodeId;

    @Column(name = "job_code", length = 20, nullable = false)
    private String jobCode;

    @Column(length = 20, nullable = false)
    private String jobName;

    @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestPeriod> restPeriods;

    @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IndustrialAccident> industrialAccidents;

    @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;

    @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resume> resumes;

    @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobPost> jobPosts;
}