package com.example.workerManagers.domain.chat.entity;

import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id")
    private JobPost jobPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    private LocalDateTime createdAt;

    @Builder
    public ChatRoom(JobPost jobPost, User applicant, User recruiter) {
        this.jobPost = jobPost;
        this.applicant = applicant;
        this.recruiter = recruiter;
        this.createdAt = LocalDateTime.now();
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public void setRecruiter(User recruiter) {
        this.recruiter = recruiter;
    }
} 