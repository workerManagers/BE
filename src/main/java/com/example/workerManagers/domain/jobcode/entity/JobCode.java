package com.example.workerManagers.domain.jobcode.entity;

import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.restperiod.entity.RestPeriod;
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

    @Column(length = 20, nullable = false, unique = true)
    private String jobName;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_category", nullable = false)
    private IndustryCategory industryCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_subcategory", nullable = false)
    private IndustrySubcategory industrySubcategory;

    @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestPeriod> restPeriods;

    @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IndustrialAccident> industrialAccidents;

    @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;

    @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobPost> jobPosts;

    @PrePersist
    @PreUpdate
    private void validateCategories() {
        if (industrySubcategory.getMainCategory() != industryCategory) {
            throw new IllegalStateException("하위 카테고리는 상위 카테고리에 속해야 합니다.");
        }
    }
}