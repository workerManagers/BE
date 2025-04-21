package com.example.workerManagers.domain.resume.entity;

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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "resume_text", nullable = false, length = 2000)
    private String resumeText;

    @Builder
    public Resume(User user, String resumeText) {
        this.user = user;
        this.resumeText = resumeText;
    }

    public void updateResumeText(String resumeText) {
        this.resumeText = resumeText;
    }
}