package com.example.workerManagers.domain.chat.entity;

import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public ChatRoom(JobPost jobPost, User applicant, User recruiter) {
        this.jobPost = jobPost;
        this.applicant = applicant;
        this.recruiter = recruiter;
        this.createdAt = LocalDateTime.now();
        this.messages = new ArrayList<>();
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public void setRecruiter(User recruiter) {
        this.recruiter = recruiter;
    }
} 