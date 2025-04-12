package com.example.workerManagers.domain.company.controller;

import com.example.workerManagers.domain.company.dto.CompanyRequestDto;
import com.example.workerManagers.domain.company.dto.CompanyResponseDto;
import com.example.workerManagers.domain.company.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(@Valid @RequestBody CompanyRequestDto requestDto) {
        CompanyResponseDto responseDto = companyService.createCompany(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompany(@PathVariable Long companyId) {
        CompanyResponseDto responseDto = companyService.getCompany(companyId);
        return ResponseEntity.ok(responseDto);
    }
} 