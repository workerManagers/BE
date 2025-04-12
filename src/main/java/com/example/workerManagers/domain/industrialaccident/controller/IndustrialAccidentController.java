package com.example.workerManagers.domain.industrialaccident.controller;

import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentRequestDto;
import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentResponseDto;
import com.example.workerManagers.domain.industrialaccident.service.IndustrialAccidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/industrial-accidents")
@RequiredArgsConstructor
public class IndustrialAccidentController {

    private final IndustrialAccidentService industrialAccidentService;

    @PostMapping
    public ResponseEntity<IndustrialAccidentResponseDto> createIndustrialAccident(
            @Valid @RequestBody IndustrialAccidentRequestDto requestDto) {
        IndustrialAccidentResponseDto responseDto = industrialAccidentService.createIndustrialAccident(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{industrialAccidentId}")
    public ResponseEntity<IndustrialAccidentResponseDto> getIndustrialAccident(
            @PathVariable Long industrialAccidentId) {
        IndustrialAccidentResponseDto responseDto = industrialAccidentService.getIndustrialAccident(industrialAccidentId);
        return ResponseEntity.ok(responseDto);
    }
} 