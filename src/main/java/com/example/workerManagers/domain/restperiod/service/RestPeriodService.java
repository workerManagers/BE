package com.example.workerManagers.domain.restperiod.service;

import com.example.workerManagers.domain.restperiod.dto.RestPeriodRequestDto;
import com.example.workerManagers.domain.restperiod.dto.RestPeriodResponseDto;

public interface RestPeriodService {
    RestPeriodResponseDto predictRestPeriod(RestPeriodRequestDto request, String userEmail);
}
