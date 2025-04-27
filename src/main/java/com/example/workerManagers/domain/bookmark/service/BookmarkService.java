package com.example.workerManagers.domain.bookmark.service;

import com.example.workerManagers.domain.bookmark.dto.BookmarkListResponseDto;
import com.example.workerManagers.domain.bookmark.dto.BookmarkRequestDto;
import com.example.workerManagers.domain.bookmark.dto.BookmarkResponseDto;

public interface BookmarkService {
    BookmarkResponseDto createBookmark(BookmarkRequestDto requestDto, String userEmail);
    void deleteBookmark(Long bookmarkId, String userEmail);
    BookmarkListResponseDto getMyBookmarks(String userEmail);
} 