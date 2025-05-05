package com.example.workerManagers.domain.aimatching.controller;

import com.example.workerManagers.domain.aimatching.dto.AIMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.AIMatchingResponseDto;
import com.example.workerManagers.domain.aimatching.dto.JobPostMatchingDto;
import com.example.workerManagers.domain.aimatching.service.AIMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-matchings")
public class AIMatchingController {

    private final AIMatchingService aiMatchingService;

    @PostMapping("/match")
    public CompletableFuture<ResponseEntity<List<AIMatchingResponseDto>>> getMatchingScores(
            @RequestBody AIMatchingRequestDto requestDto) {
        return aiMatchingService.getMatchingScores(requestDto)
                .thenApply(ResponseEntity::ok);
    }
} 