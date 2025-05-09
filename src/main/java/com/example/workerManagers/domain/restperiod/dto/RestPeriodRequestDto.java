// src/main/java/com/example/workerManagers/domain/restperiod/dto/RestPeriodRequestDto.java
package com.example.workerManagers.domain.restperiod.dto;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 추가
public class RestPeriodRequestDto {
    @NotNull(message = "질병명은 필수입니다.")
    private String disease;
    
    @NotNull(message = "성별은 필수입니다.")
    private String sex;
    
    @NotNull(message = "수술 여부는 필수입니다.")
    private String surgery;
    
    @NotNull(message = "연령대는 필수입니다.")
    private String age;
    
    @NotNull(message = "지역은 필수입니다.")
    private String region;
    
    @Override
    public String toString() {
        return "RestPeriodRequestDto{" +
                "disease='" + disease + '\'' +
                ", sex='" + sex + '\'' +
                ", surgery='" + surgery + '\'' +
                ", age='" + age + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
