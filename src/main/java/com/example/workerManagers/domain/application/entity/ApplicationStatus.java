package com.example.workerManagers.domain.application.entity;

public enum ApplicationStatus {
    APPLIED("지원완료"),
    DOCUMENT_PASSED("서류통과"),
    INTERVIEW_SCHEDULED("면접예정"),
    INTERVIEW_PASSED("면접통과"),
    INTERVIEW_FAILED("면접불합격"),
    HIRED("채용완료"),
    REJECTED("불합격");
    
    private final String description;
    
    ApplicationStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 