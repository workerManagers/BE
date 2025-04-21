package com.example.workerManagers.domain.aimatching.controller;

import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingResponseDto;
import com.example.workerManagers.domain.aimatching.service.CompanyMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company-matchings")
public class CompanyMatchingController {

    private final CompanyMatchingService companyMatchingService;

    @PostMapping("/match")
    public ResponseEntity<List<CompanyMatchingResponseDto>> getMatchingScoresForResumes(
            @RequestBody CompanyMatchingRequestDto requestDto) {
        return ResponseEntity.ok(companyMatchingService.getMatchingScoresForResumes(requestDto));
    }
} 