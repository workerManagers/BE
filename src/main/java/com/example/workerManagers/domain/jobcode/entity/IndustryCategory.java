package com.example.workerManagers.domain.jobcode.entity;

public enum IndustryCategory {
    PRODUCTION_CONSTRUCTION_LABOR("생산·건설·노무"),
    DRIVING_DELIVERY("운전·배달"),
    HOSPITAL_NURSING_RESEARCH("병원·간호·연구");

    private final String description;

    IndustryCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 