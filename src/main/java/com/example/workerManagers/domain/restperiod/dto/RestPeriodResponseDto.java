// src/main/java/com/example/workerManagers/domain/restperiod/dto/RestPeriodResponseDto.java
package com.example.workerManagers.domain.restperiod.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class RestPeriodResponseDto {
    private int predictedPeriod;
    private String message;
    
    @Override
    public String toString() {
        return "RestPeriodResponseDto{" +
                "predictedPeriod=" + predictedPeriod +
                ", message='" + message + '\'' +
                '}';
    }
}