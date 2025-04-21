package com.example.workerManagers.domain.restperiod.entity;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.industrialaccident.entity.IndustrialAccident;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rest_periods")
public class RestPeriod {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restPeriod_id;
    
    @Column(nullable = false)
    private String disease;
    
    @Column(nullable = false)
    private String sex;
    
    @Column(nullable = false)
    private String surgery;
    
    @Column(nullable = false)
    private String age;
    
    @Column(nullable = false)
    private String region;
    
    @Column(nullable = false)
    private int predictedPeriod;
    
    @Column(nullable = false)
    private LocalDate predictionDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industrial_accident_id")
    private IndustrialAccident industrialAccident;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code_id")
    private JobCode jobCode;
}
