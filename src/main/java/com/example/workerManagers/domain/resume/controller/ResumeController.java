package com.example.workerManagers.domain.resume.controller;

import com.example.workerManagers.domain.resume.dto.ResumeRequestDto;
import com.example.workerManagers.domain.resume.dto.ResumeResponseDto;
import com.example.workerManagers.domain.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<?> createResume(
            @RequestBody ResumeRequestDto requestDto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        try {
            return ResponseEntity.ok(resumeService.createResume(requestDto, userEmail));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 이력서가 존재합니다. 수정하거나 삭제 후 다시 시도해주세요.");
        }
    }

    @GetMapping
    public ResponseEntity<ResumeResponseDto> getResume(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(resumeService.getResume(userEmail));
    }

    @PutMapping
    public ResponseEntity<ResumeResponseDto> updateResume(
            @RequestBody ResumeRequestDto requestDto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(resumeService.updateResume(requestDto, userEmail));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteResume(Authentication authentication) {
        String userEmail = authentication.getName();
        resumeService.deleteResume(userEmail);
        return ResponseEntity.ok().build();
    }
} 