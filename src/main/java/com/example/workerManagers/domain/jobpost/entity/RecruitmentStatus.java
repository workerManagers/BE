package com.example.workerManagers.domain.jobpost.entity;

public enum RecruitmentStatus {
    OPEN("모집중"),
    CLOSED("모집마감"),
    HIRED("채용완료"),
    CANCELLED("취소됨");

    private final String description;

    RecruitmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 