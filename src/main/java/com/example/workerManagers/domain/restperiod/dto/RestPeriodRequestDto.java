// src/main/java/com/example/workerManagers/domain/restperiod/dto/RestPeriodRequestDto.java
package com.example.workerManagers.domain.restperiod.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class RestPeriodRequestDto {
    @NotNull
    private String disease;
    
    @NotNull
    private String sex;
    
    @NotNull
    private String surgery;
    
    @NotNull
    private String age;
    
    @NotNull
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
