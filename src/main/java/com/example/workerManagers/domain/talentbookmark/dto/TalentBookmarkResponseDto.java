package com.example.workerManagers.domain.talentbookmark.dto;

import com.example.workerManagers.domain.talentbookmark.entity.TalentBookmark;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class TalentBookmarkResponseDto {
    private Long talentBookmarkId;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDate createdAt;

    public static TalentBookmarkResponseDto from(TalentBookmark talentBookmark) {
        return TalentBookmarkResponseDto.builder()
                .talentBookmarkId(talentBookmark.getTalentBookmarkId())
                .userId(talentBookmark.getUser().getUserId())
                .userName(talentBookmark.getUser().getUserName())
                .userEmail(talentBookmark.getUser().getUserEmail())
                .createdAt(talentBookmark.getCreatedAt())
                .build();
    }
} 