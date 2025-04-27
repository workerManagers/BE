package com.example.workerManagers.domain.talentbookmark.service;

import com.example.workerManagers.domain.talentbookmark.dto.TalentBookmarkListResponseDto;
import com.example.workerManagers.domain.talentbookmark.dto.TalentBookmarkRequestDto;
import com.example.workerManagers.domain.talentbookmark.dto.TalentBookmarkResponseDto;

public interface TalentBookmarkService {
    TalentBookmarkResponseDto createTalentBookmark(TalentBookmarkRequestDto requestDto, String companyEmail);
    void deleteTalentBookmark(Long talentBookmarkId, String companyEmail);
    TalentBookmarkListResponseDto getMyTalentBookmarks(String companyEmail);
} 