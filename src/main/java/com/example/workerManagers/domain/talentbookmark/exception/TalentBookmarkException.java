package com.example.workerManagers.domain.talentbookmark.exception;

public class TalentBookmarkException extends RuntimeException {
    public static final String NOT_FOUND_USER = "존재하지 않는 인재입니다.";
    public static final String ALREADY_BOOKMARKED = "이미 찜한 인재입니다.";
    public static final String NOT_OWNED_BOOKMARK = "본인의 찜한 인재만 삭제할 수 있습니다.";
    public static final String NOT_FOUND_BOOKMARK = "존재하지 않는 찜한 인재입니다.";

    public TalentBookmarkException(String message) {
        super(message);
    }
} 