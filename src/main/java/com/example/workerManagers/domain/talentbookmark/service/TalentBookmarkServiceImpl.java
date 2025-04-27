package com.example.workerManagers.domain.talentbookmark.service;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.domain.talentbookmark.dto.TalentBookmarkListResponseDto;
import com.example.workerManagers.domain.talentbookmark.dto.TalentBookmarkRequestDto;
import com.example.workerManagers.domain.talentbookmark.dto.TalentBookmarkResponseDto;
import com.example.workerManagers.domain.talentbookmark.entity.TalentBookmark;
import com.example.workerManagers.domain.talentbookmark.exception.TalentBookmarkException;
import com.example.workerManagers.domain.talentbookmark.repository.TalentBookmarkRepository;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TalentBookmarkServiceImpl implements TalentBookmarkService {

    private final TalentBookmarkRepository talentBookmarkRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TalentBookmarkResponseDto createTalentBookmark(TalentBookmarkRequestDto requestDto, String companyEmail) {
        Company company = companyRepository.findByUser(
                userRepository.findByUserEmail(companyEmail)
                        .orElseThrow(() -> new TalentBookmarkException("기업 사용자를 찾을 수 없습니다."))
        ).orElseThrow(() -> new TalentBookmarkException("기업 정보를 찾을 수 없습니다."));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new TalentBookmarkException(TalentBookmarkException.NOT_FOUND_USER));

        if (talentBookmarkRepository.existsByCompanyAndUser(company, user)) {
            throw new TalentBookmarkException(TalentBookmarkException.ALREADY_BOOKMARKED);
        }

        TalentBookmark talentBookmark = TalentBookmark.builder()
                .company(company)
                .user(user)
                .build();

        TalentBookmark saved = talentBookmarkRepository.save(talentBookmark);
        return TalentBookmarkResponseDto.from(saved);
    }

    @Override
    @Transactional
    public void deleteTalentBookmark(Long talentBookmarkId, String companyEmail) {
        Company company = companyRepository.findByUser(
                userRepository.findByUserEmail(companyEmail)
                        .orElseThrow(() -> new TalentBookmarkException("기업 사용자를 찾을 수 없습니다."))
        ).orElseThrow(() -> new TalentBookmarkException("기업 정보를 찾을 수 없습니다."));

        TalentBookmark talentBookmark = talentBookmarkRepository.findById(talentBookmarkId)
                .orElseThrow(() -> new TalentBookmarkException(TalentBookmarkException.NOT_FOUND_BOOKMARK));

        if (!talentBookmark.getCompany().equals(company)) {
            throw new TalentBookmarkException(TalentBookmarkException.NOT_OWNED_BOOKMARK);
        }

        talentBookmarkRepository.delete(talentBookmark);
    }

    @Override
    @Transactional(readOnly = true)
    public TalentBookmarkListResponseDto getMyTalentBookmarks(String companyEmail) {
        Company company = companyRepository.findByUser(
                userRepository.findByUserEmail(companyEmail)
                        .orElseThrow(() -> new TalentBookmarkException("기업 사용자를 찾을 수 없습니다."))
        ).orElseThrow(() -> new TalentBookmarkException("기업 정보를 찾을 수 없습니다."));

        List<TalentBookmark> bookmarks = talentBookmarkRepository.findByCompany(company);
        List<TalentBookmarkResponseDto> dtos = bookmarks.stream()
                .map(TalentBookmarkResponseDto::from)
                .collect(Collectors.toList());

        return TalentBookmarkListResponseDto.builder()
                .talentBookmarks(dtos)
                .build();
    }
} 