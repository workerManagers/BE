package com.example.workerManagers.domain.industrialaccident.controller;

import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentRequestDto;
import com.example.workerManagers.domain.industrialaccident.dto.IndustrialAccidentResponseDto;
import com.example.workerManagers.domain.industrialaccident.service.IndustrialAccidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/industrial-accidents")
@RequiredArgsConstructor
public class IndustrialAccidentController {
    private final IndustrialAccidentService industrialAccidentService;

    @PostMapping
    public ResponseEntity<IndustrialAccidentResponseDto> createIndustrialAccident(@RequestBody IndustrialAccidentRequestDto requestDto) {
        return ResponseEntity.ok(industrialAccidentService.createIndustrialAccident(requestDto));
    }

    @GetMapping("/{industrialAccidentId}")
    public ResponseEntity<IndustrialAccidentResponseDto> getIndustrialAccident(@PathVariable Long industrialAccidentId) {
        return ResponseEntity.ok(industrialAccidentService.getIndustrialAccident(industrialAccidentId));
    }

    @PutMapping("/{industrialAccidentId}")
    public ResponseEntity<IndustrialAccidentResponseDto> updateIndustrialAccident(@PathVariable Long industrialAccidentId, @RequestBody IndustrialAccidentRequestDto requestDto) {
        return ResponseEntity.ok(industrialAccidentService.updateIndustrialAccident(industrialAccidentId, requestDto));
    }

    @GetMapping
    public ResponseEntity<List<IndustrialAccidentResponseDto>> getAllIndustrialAccidents() {
        return ResponseEntity.ok(industrialAccidentService.getAllIndustrialAccidents());
    }
} 