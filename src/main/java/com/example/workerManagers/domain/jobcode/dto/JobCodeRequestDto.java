package com.example.workerManagers.domain.jobcode.dto;

import com.example.workerManagers.domain.jobcode.entity.IndustryCategory;
import com.example.workerManagers.domain.jobcode.entity.IndustrySubcategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCodeRequestDto {
    @NotBlank(message = "직종명은 필수 입력값입니다.")
    @Size(max = 20, message = "직종명은 20자를 초과할 수 없습니다.")
    private String jobName;

    @NotNull(message = "산업 카테고리는 필수 입력값입니다.")
    private IndustryCategory industryCategory;

    @NotNull(message = "산업 하위 카테고리는 필수 입력값입니다.")
    private IndustrySubcategory industrySubcategory;
} 