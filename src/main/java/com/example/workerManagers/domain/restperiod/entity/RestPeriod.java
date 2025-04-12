package com.example.workerManagers.domain.restperiod.entity;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restPeriodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industrialAccident_id", nullable = false)
    private IndustrialAccident industrialAccident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code", nullable = false)
    private JobCode jobCode;

    private LocalDateTime restPeriodDatetime;
}