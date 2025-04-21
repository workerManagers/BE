package com.example.workerManagers.domain.company.entity;

import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.restperiod.entity.RestPeriod;
import com.example.workerManagers.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(nullable = false, length = 100)
    private String companyName;

    @Column(nullable = false, length = 50)
    private String companyRegion;

    @Column(length = 20)
    private String companyCode;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<IndustrialAccident> industrialAccidents;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<RestPeriod> restPeriods;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<JobPost> jobPosts;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Application> applications;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void update(String companyName, String companyRegion, String companyCode) {
        this.companyName = companyName;
        this.companyRegion = companyRegion;
        this.companyCode = companyCode;
    }
}