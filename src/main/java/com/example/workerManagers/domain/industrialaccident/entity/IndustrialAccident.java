package com.example.workerManagers.domain.industrialaccident.entity;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.example.workerManagers.domain.restperiod.entity.RestPeriod;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "industrial_accident")
public class IndustrialAccident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "industrial_accident_id")
    private Long industrialAccidentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code")
    private JobCode jobCode;

    @Column(name = "industrial_accident_code", nullable = false)
    private String industrialAccidentCode;

    @Column(name = "industrial_accident_name", nullable = false)
    private String industrialAccidentName;

    @Column(name = "industrial_accident_date", nullable = false)
    private LocalDateTime industrialAccidentDate;

    @OneToMany(mappedBy = "industrialAccident", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestPeriod> restPeriods = new HashSet<>();

    @Builder
    public IndustrialAccident(Company company, JobCode jobCode, String industrialAccidentCode,
                             String industrialAccidentName, LocalDateTime industrialAccidentDate) {
        this.company = company;
        this.jobCode = jobCode;
        this.industrialAccidentCode = industrialAccidentCode;
        this.industrialAccidentName = industrialAccidentName;
        this.industrialAccidentDate = industrialAccidentDate;
    }

    public void update(Company company, JobCode jobCode, String industrialAccidentCode,
                      String industrialAccidentName, LocalDateTime industrialAccidentDate) {
        this.company = company;
        this.jobCode = jobCode;
        this.industrialAccidentCode = industrialAccidentCode;
        this.industrialAccidentName = industrialAccidentName;
        this.industrialAccidentDate = industrialAccidentDate;
    }
} 