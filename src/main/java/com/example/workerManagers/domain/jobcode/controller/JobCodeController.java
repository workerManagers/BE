package com.example.workerManagers.domain.jobcode.controller;

import com.example.workerManagers.domain.jobcode.dto.JobCodeRequestDto;
import com.example.workerManagers.domain.jobcode.dto.JobCodeResponseDto;
import com.example.workerManagers.domain.jobcode.service.JobCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-codes")
@RequiredArgsConstructor
public class JobCodeController {

    private final JobCodeService jobCodeService;

    @PostMapping
    public ResponseEntity<JobCodeResponseDto> createJobCode(@Valid @RequestBody JobCodeRequestDto requestDto) {
        JobCodeResponseDto responseDto = jobCodeService.createJobCode(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{jobCodeId}")
    public ResponseEntity<JobCodeResponseDto> getJobCode(@PathVariable Long jobCodeId) {
        JobCodeResponseDto responseDto = jobCodeService.getJobCode(jobCodeId);
        return ResponseEntity.ok(responseDto);
    }
} 