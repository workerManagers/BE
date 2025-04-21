// src/main/java/com/example/workerManagers/domain/restperiod/controller/RestPeriodController.java
package com.example.workerManagers.domain.restperiod.controller;

import com.example.workerManagers.domain.restperiod.dto.RestPeriodRequestDto;
import com.example.workerManagers.domain.restperiod.dto.RestPeriodResponseDto;
import com.example.workerManagers.domain.restperiod.service.RestPeriodService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/predict")
@RequiredArgsConstructor
public class RestPeriodController {
    
    private static final Logger logger = LoggerFactory.getLogger(RestPeriodController.class);
    private final RestPeriodService restPeriodService;
    
    @PostMapping
    public ResponseEntity<RestPeriodResponseDto> predictRestPeriod(
            @Valid @RequestBody RestPeriodRequestDto request,
            HttpServletRequest httpRequest) {
        logger.info("요양기간 예측 요청 수신 - URI: {}, Method: {}, RemoteAddr: {}", 
                httpRequest.getRequestURI(), 
                httpRequest.getMethod(), 
                httpRequest.getRemoteAddr());
        logger.info("요청 데이터: {}", request);
        
        try {
            RestPeriodResponseDto response = restPeriodService.predictRestPeriod(request);
            logger.info("요양기간 예측 응답: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("요양기간 예측 중 오류 발생: ", e);
            throw e;
        }
    }
}