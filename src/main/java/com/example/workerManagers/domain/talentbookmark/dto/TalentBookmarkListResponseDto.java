package com.example.workerManagers.domain.talentbookmark.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class TalentBookmarkListResponseDto {
    private List<TalentBookmarkResponseDto> talentBookmarks;
} 