package com.example.workerManagers.domain.bookmark.entity;

import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "bookmark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Builder
    public Bookmark(User user, JobPost jobPost) {
        this.user = user;
        this.jobPost = jobPost;
        this.createdAt = LocalDate.now();
    }
} 