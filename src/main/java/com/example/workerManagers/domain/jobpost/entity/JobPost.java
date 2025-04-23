package com.example.workerManagers.domain.jobpost.entity;

import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "job_post")
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_post_id")
    private Long jobPostId;

    @Column(name = "job_name")
    private String jobName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code_id", nullable = false)
    private JobCode jobCode;

    @Column(name = "job_post_description", nullable = false, length = 1000)
    private String jobPostDescription;

    @Column(name = "job_period", nullable = false, length = 50)
    private String jobPeriod;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CareerType careerType;

    @Column(name = "main_tasks", nullable = false, length = 1000)
    private String mainTasks;

    @Column(name = "qualifications", nullable = false, length = 1000)
    private String qualifications;

    @Column(name = "preferred_qualifications", nullable = false, length = 1000)
    private String preferredQualifications;

    @Column(name = "ideal_candidate", nullable = false, length = 1000)
    private String idealCandidate;

    @Column(name = "job_region", nullable = false, length = 50)
    private String jobRegion;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    @Builder
    public JobPost(String jobName, Company company, JobCode jobCode, String jobPostDescription, String jobPeriod,
                  LocalDateTime deadline, CareerType careerType, String mainTasks, String qualifications,
                  String preferredQualifications, String idealCandidate, String jobRegion) {
        this.jobName = jobName;
        this.company = company;
        this.jobCode = jobCode;
        this.jobPostDescription = jobPostDescription;
        this.jobPeriod = jobPeriod;
        this.deadline = deadline;
        this.careerType = careerType;
        this.mainTasks = mainTasks;
        this.qualifications = qualifications;
        this.preferredQualifications = preferredQualifications;
        this.idealCandidate = idealCandidate;
        this.jobRegion = jobRegion;
    }

    public void update(Company company, JobCode jobCode, String jobPostDescription, String jobPeriod,
                      LocalDateTime deadline, CareerType careerType, String mainTasks, String qualifications,
                      String preferredQualifications, String idealCandidate, String jobRegion) {
        this.company = company;
        this.jobCode = jobCode;
        this.jobPostDescription = jobPostDescription;
        this.jobPeriod = jobPeriod;
        this.deadline = deadline;
        this.careerType = careerType;
        this.mainTasks = mainTasks;
        this.qualifications = qualifications;
        this.preferredQualifications = preferredQualifications;
        this.idealCandidate = idealCandidate;
        this.jobRegion = jobRegion;
    }
}