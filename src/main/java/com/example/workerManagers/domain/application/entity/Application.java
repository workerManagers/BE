package com.example.workerManagers.domain.application.entity;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

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

    @Column(name = "job_description", length = 100)
    private String jobDescription;

    @Column(name = "job_period", length = 50)
    private String jobPeriod;

    @Column(name = "apply_result")
    private String applyResult;

    @Builder
    public Application(Company company, JobCode jobCode, JobPost jobPost, User user, 
                      String jobDescription, String jobPeriod, String applyResult) {
        this.company = company;
        this.jobCode = jobCode;
        this.jobPost = jobPost;
        this.user = user;
        this.jobDescription = jobDescription;
        this.jobPeriod = jobPeriod;
        this.applyResult = applyResult;
    }

    public void update(Company company, JobCode jobCode, JobPost jobPost, User user,
                      String jobDescription, String jobPeriod, String applyResult) {
        this.company = company;
        this.jobCode = jobCode;
        this.jobPost = jobPost;
        this.user = user;
        this.jobDescription = jobDescription;
        this.jobPeriod = jobPeriod;
        this.applyResult = applyResult;
    }
}