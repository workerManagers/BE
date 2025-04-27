package com.example.workerManagers.domain.talentbookmark.controller;

import com.example.workerManagers.domain.talentbookmark.dto.TalentBookmarkListResponseDto;
import com.example.workerManagers.domain.talentbookmark.dto.TalentBookmarkRequestDto;
import com.example.workerManagers.domain.talentbookmark.dto.TalentBookmarkResponseDto;
import com.example.workerManagers.domain.talentbookmark.service.TalentBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/talentbookmarks")
@RequiredArgsConstructor
public class TalentBookmarkController {
    private final TalentBookmarkService talentBookmarkService;

    @PostMapping
    public ResponseEntity<TalentBookmarkResponseDto> createTalentBookmark(@RequestBody TalentBookmarkRequestDto requestDto, Authentication authentication) {
        String companyEmail = authentication.getName();
        TalentBookmarkResponseDto responseDto = talentBookmarkService.createTalentBookmark(requestDto, companyEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{talentBookmarkId}")
    public ResponseEntity<Void> deleteTalentBookmark(@PathVariable Long talentBookmarkId, Authentication authentication) {
        String companyEmail = authentication.getName();
        talentBookmarkService.deleteTalentBookmark(talentBookmarkId, companyEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<TalentBookmarkListResponseDto> getMyTalentBookmarks(Authentication authentication) {
        String companyEmail = authentication.getName();
        TalentBookmarkListResponseDto responseDto = talentBookmarkService.getMyTalentBookmarks(companyEmail);
        return ResponseEntity.ok(responseDto);
    }
} 