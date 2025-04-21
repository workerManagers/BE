package com.example.workerManagers.domain.application.controller;

import com.example.workerManagers.domain.application.dto.ApplicationRequestDto;
import com.example.workerManagers.domain.application.dto.ApplicationResponseDto;
import com.example.workerManagers.domain.application.dto.ApplicationStatusUpdateDto;
import com.example.workerManagers.domain.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponseDto> createApplication(
            @RequestBody ApplicationRequestDto requestDto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(applicationService.createApplication(requestDto, userEmail));
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<ApplicationResponseDto>> getMyApplications(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(applicationService.getMyApplications(userEmail));
    }

    @GetMapping("/job-posts/{jobPostId}")
    public ResponseEntity<List<ApplicationResponseDto>> getJobPostApplications(
            @PathVariable Long jobPostId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(applicationService.getJobPostApplications(jobPostId, userEmail));
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponseDto> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody ApplicationStatusUpdateDto requestDto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, requestDto, userEmail));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long applicationId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        applicationService.deleteApplication(applicationId, userEmail);
        return ResponseEntity.ok().build();
    }
} 