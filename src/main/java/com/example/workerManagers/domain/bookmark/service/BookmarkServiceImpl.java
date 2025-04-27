package com.example.workerManagers.domain.bookmark.service;

import com.example.workerManagers.domain.bookmark.dto.BookmarkListResponseDto;
import com.example.workerManagers.domain.bookmark.dto.BookmarkRequestDto;
import com.example.workerManagers.domain.bookmark.dto.BookmarkResponseDto;
import com.example.workerManagers.domain.bookmark.entity.Bookmark;
import com.example.workerManagers.domain.bookmark.exception.BookmarkException;
import com.example.workerManagers.domain.bookmark.repository.BookmarkRepository;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final JobPostRepository jobPostRepository;

    @Override
    @Transactional
    public BookmarkResponseDto createBookmark(BookmarkRequestDto requestDto, String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new BookmarkException("사용자를 찾을 수 없습니다."));

        JobPost jobPost = jobPostRepository.findById(requestDto.getJobPostId())
                .orElseThrow(() -> new BookmarkException("채용 공고를 찾을 수 없습니다."));

        if (bookmarkRepository.existsByUserAndJobPost(user, jobPost)) {
            throw new BookmarkException("이미 찜한 채용 공고입니다.");
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .jobPost(jobPost)
                .build();

        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        return BookmarkResponseDto.from(savedBookmark);
    }

    @Override
    @Transactional
    public void deleteBookmark(Long bookmarkId, String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new BookmarkException("사용자를 찾을 수 없습니다."));

        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkException("찜한 공고를 찾을 수 없습니다."));

        if (!bookmark.getUser().equals(user)) {
            throw new BookmarkException("다른 사용자의 찜한 공고는 삭제할 수 없습니다.");
        }

        bookmarkRepository.delete(bookmark);
    }

    @Override
    @Transactional(readOnly = true)
    public BookmarkListResponseDto getMyBookmarks(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new BookmarkException("사용자를 찾을 수 없습니다."));

        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user);
        List<BookmarkResponseDto> bookmarkDtos = bookmarks.stream()
                .map(BookmarkResponseDto::from)
                .collect(Collectors.toList());

        return BookmarkListResponseDto.builder()
                .bookmarks(bookmarkDtos)
                .build();
    }
} 