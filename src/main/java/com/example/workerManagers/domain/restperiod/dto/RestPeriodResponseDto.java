// src/main/java/com/example/workerManagers/domain/restperiod/dto/RestPeriodResponseDto.java
package com.example.workerManagers.domain.restperiod.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class RestPeriodResponseDto {
    @JsonProperty("restPeriod_id")
    private Long restPeriodId;
    
    @JsonProperty("predicted_value")
    private int predictedValue;
    private String message;
    
    @Override
    public String toString() {
        return "RestPeriodResponseDto{" +
                "restPeriodId=" + restPeriodId +
                ", predictedValue=" + predictedValue +
                ", message='" + message + '\'' +
                '}';
    }
}