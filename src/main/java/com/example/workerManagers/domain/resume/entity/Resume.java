package com.example.workerManagers.domain.resume.entity;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "resume")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long resumeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id")
    private JobPost jobPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code")
    private JobCode jobCode;

    @Column(name = "resume_area", length = 20)
    private String resumeArea;

    @Column(name = "resume_history", length = 100)
    private String resumeHistory;

    @Builder
    public Resume(User user, JobPost jobPost, Company company, 
                 JobCode jobCode, String resumeArea, String resumeHistory) {
        this.user = user;
        this.jobPost = jobPost;
        this.company = company;
        this.jobCode = jobCode;
        this.resumeArea = resumeArea;
        this.resumeHistory = resumeHistory;
    }

    public void update(User user, JobPost jobPost, Company company,
                      JobCode jobCode, String resumeArea, String resumeHistory) {
        this.user = user;
        this.jobPost = jobPost;
        this.company = company;
        this.jobCode = jobCode;
        this.resumeArea = resumeArea;
        this.resumeHistory = resumeHistory;
    }
}