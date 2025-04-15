package com.example.workerManagers.domain.industrialaccident.service;

import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentRequestDto;
import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentResponseDto;

import java.util.List;

public interface IndustrialAccidentService {
    IndustrialAccidentResponseDto createIndustrialAccident(IndustrialAccidentRequestDto requestDto);
    IndustrialAccidentResponseDto getIndustrialAccident(Long industrialAccidentId);
    IndustrialAccidentResponseDto updateIndustrialAccident(Long industrialAccidentId, IndustrialAccidentRequestDto requestDto);
    List<IndustrialAccidentResponseDto> getAllIndustrialAccidents();
} 