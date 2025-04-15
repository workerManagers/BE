package com.example.workerManagers.domain.industrialaccident.entity;

import com.example.workerManagers.domain.aimatching.entity.AIMatching;
import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.restperiod.entity.RestPeriod;
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
public class IndustrialAccident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long industrialAccidentId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "job_code")
    private JobCode jobCode;

    private String industrialAccidentCode;
    private String industrialAccidentName;
    private LocalDateTime industrialAccidentDate;

    @OneToMany(mappedBy = "industrialAccident", cascade = CascadeType.ALL)
    private Set<RestPeriod> restPeriods;

    @OneToMany(mappedBy = "industrialAccident", cascade = CascadeType.ALL)
    private Set<JobPost> jobPosts;

    @OneToMany(mappedBy = "industrialAccident", cascade = CascadeType.ALL)
    private Set<AIMatching> aiMatchings;

    @OneToMany(mappedBy = "industrialAccident", cascade = CascadeType.ALL)
    private Set<Application> applications;

    @OneToMany(mappedBy = "industrialAccident", cascade = CascadeType.ALL)
    private Set<Resume> resumes;

    public void update(Company company, JobCode jobCode, String industrialAccidentCode,
                      String industrialAccidentName, LocalDateTime industrialAccidentDate) {
        this.company = company;
        this.jobCode = jobCode;
        this.industrialAccidentCode = industrialAccidentCode;
        this.industrialAccidentName = industrialAccidentName;
        this.industrialAccidentDate = industrialAccidentDate;
    }
} 