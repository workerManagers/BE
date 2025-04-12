package com.example.workerManagers.domain.industrialaccident.service;

import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentRequestDto;
import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentResponseDto;

public interface IndustrialAccidentService {
    IndustrialAccidentResponseDto createIndustrialAccident(IndustrialAccidentRequestDto requestDto);
    IndustrialAccidentResponseDto getIndustrialAccident(Long industrialAccidentId);
} 