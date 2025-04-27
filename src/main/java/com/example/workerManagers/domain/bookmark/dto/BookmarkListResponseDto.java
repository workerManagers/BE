package com.example.workerManagers.domain.bookmark.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookmarkListResponseDto {
    private List<BookmarkResponseDto> bookmarks;
} 