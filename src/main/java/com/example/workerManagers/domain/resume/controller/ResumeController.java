package com.example.workerManagers.domain.resume.controller;

import com.example.workerManagers.domain.resume.dto.ResumeRequestDto;
import com.example.workerManagers.domain.resume.dto.ResumeResponseDto;
import com.example.workerManagers.domain.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> getResume(Authentication authentication) {
        String userEmail = authentication.getName();
        try {
            ResumeResponseDto responseDto = resumeService.getResume(userEmail);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "생성된 이력서가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateResume(
            @RequestBody ResumeRequestDto requestDto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        try {
            return ResponseEntity.ok(resumeService.updateResume(requestDto, userEmail));
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "수정할 이력서가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteResume(Authentication authentication) {
        String userEmail = authentication.getName();
        try {
            resumeService.deleteResume(userEmail);
            Map<String, String> response = new HashMap<>();
            response.put("message", "이력서가 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "삭제할 이력서가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
} 