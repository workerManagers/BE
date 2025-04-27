package com.example.workerManagers.domain.bookmark.dto;

import com.example.workerManagers.domain.bookmark.entity.Bookmark;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BookmarkResponseDto {
    private Long bookmarkId;
    private Long jobPostId;
    private String companyName;
    private String jobName;
    private LocalDate createdAt;

    public static BookmarkResponseDto from(Bookmark bookmark) {
        return BookmarkResponseDto.builder()
                .bookmarkId(bookmark.getBookmarkId())
                .jobPostId(bookmark.getJobPost().getJobPostId())
                .companyName(bookmark.getJobPost().getCompany().getCompanyName())
                .jobName(bookmark.getJobPost().getJobCode().getJobName())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
} 