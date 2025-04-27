package com.example.workerManagers.domain.talentbookmark.entity;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "talent_bookmark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TalentBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talent_bookmark_id")
    private Long talentBookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Builder
    public TalentBookmark(Company company, User user) {
        this.company = company;
        this.user = user;
        this.createdAt = LocalDate.now();
    }
} 