package com.example.workerManagers.domain.jobcode.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCodeRequestDto {
    @NotBlank(message = "직종 코드는 필수 입력값입니다.")
    @Size(max = 20, message = "직종 코드는 20자를 초과할 수 없습니다.")
    private String jobCode;

    @NotBlank(message = "직종명은 필수 입력값입니다.")
    @Size(max = 50, message = "직종명은 50자를 초과할 수 없습니다.")
    private String jobName;

    @NotBlank(message = "직종 설명은 필수 입력값입니다.")
    @Size(max = 1000, message = "직종 설명은 1000자를 초과할 수 없습니다.")
    private String jobDescription;
} 