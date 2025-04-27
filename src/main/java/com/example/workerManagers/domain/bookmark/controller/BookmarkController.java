package com.example.workerManagers.domain.bookmark.controller;

import com.example.workerManagers.domain.bookmark.dto.BookmarkListResponseDto;
import com.example.workerManagers.domain.bookmark.dto.BookmarkRequestDto;
import com.example.workerManagers.domain.bookmark.dto.BookmarkResponseDto;
import com.example.workerManagers.domain.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<BookmarkResponseDto> createBookmark(
            @RequestBody BookmarkRequestDto requestDto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(bookmarkService.createBookmark(requestDto, userEmail));
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(
            @PathVariable Long bookmarkId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        bookmarkService.deleteBookmark(bookmarkId, userEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<BookmarkListResponseDto> getMyBookmarks(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(bookmarkService.getMyBookmarks(userEmail));
    }
} 