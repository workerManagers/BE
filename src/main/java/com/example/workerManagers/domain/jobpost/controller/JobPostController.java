package com.example.workerManagers.domain.jobpost.controller;

import com.example.workerManagers.domain.jobpost.dto.JobPostRequestDto;
import com.example.workerManagers.domain.jobpost.dto.JobPostResponseDto;
import com.example.workerManagers.domain.jobpost.service.JobPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-posts")
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;

    @PostMapping
    public ResponseEntity<JobPostResponseDto> createJobPost(@Valid @RequestBody JobPostRequestDto requestDto) {
        JobPostResponseDto responseDto = jobPostService.createJobPost(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{jobPostId}")
    public ResponseEntity<JobPostResponseDto> getJobPost(@PathVariable Long jobPostId) {
        JobPostResponseDto responseDto = jobPostService.getJobPost(jobPostId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{jobPostId}")
    public ResponseEntity<JobPostResponseDto> updateJobPost(
            @PathVariable Long jobPostId,
            @Valid @RequestBody JobPostRequestDto requestDto) {
        JobPostResponseDto responseDto = jobPostService.updateJobPost(jobPostId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{jobPostId}")
    public ResponseEntity<Void> deleteJobPost(@PathVariable Long jobPostId) {
        jobPostService.deleteJobPost(jobPostId);
        return ResponseEntity.noContent().build();
    }
} 