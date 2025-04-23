package com.example.workerManagers.domain.jobcode.dto;

import com.example.workerManagers.domain.jobcode.entity.IndustryCategory;
import com.example.workerManagers.domain.jobcode.entity.IndustrySubcategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCodeResponseDto {
    private Long jobCodeId;
    private String jobCode;
    private String jobName;
    private IndustryCategory industryCategory;
    private IndustrySubcategory industrySubcategory;
    private String message;
} 