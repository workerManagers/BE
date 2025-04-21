package com.example.workerManagers.domain.aimatching.controller;

import com.example.workerManagers.domain.aimatching.dto.JobPostMatchingDto;
import com.example.workerManagers.domain.aimatching.service.AIMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-matchings")
public class AIMatchingController {

    private final AIMatchingService aiMatchingService;

    @PostMapping("/match")
    public ResponseEntity<List<JobPostMatchingDto>> getMatchingScoresForAllJobPosts(@RequestBody String resumeText) {
        return ResponseEntity.ok(aiMatchingService.getMatchingScoresForAllJobPosts(resumeText));
    }
} 