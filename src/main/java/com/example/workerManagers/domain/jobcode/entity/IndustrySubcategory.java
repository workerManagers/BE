package com.example.workerManagers.domain.jobcode.entity;

public enum IndustrySubcategory {
    // 생산·건설·노무 하위 카테고리
    FOOD_BEVERAGE("식품·음수식품", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    TEXTILE_APPAREL("섬유·의류", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    ASSEMBLY_PRODUCTION("조립·생산직", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    MACHINERY_EQUIPMENT("기계·장비", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    CONSTRUCTION_CIVIL("토목·플랜트·건설", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    MANUFACTURING_PRODUCTION("제조·가공", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    PRINTING_PUBLISHING("인쇄·출판", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    WAREHOUSE_MATERIALS("입출고·창고관리", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    ELECTRICAL_FACILITY("전기·시설관리", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    SEMICONDUCTOR_DISPLAY("반도체·전자부품생산", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    QUALITY_AS("정비·수리·설치·A/S", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    ELECTRICAL_CONTROL("전기·제어·배관공사", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    PUBLIC_CONSTRUCTION("공사·건설현장", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    AUTOMOBILE("자동차", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    SHIPBUILDING_CONSTRUCTION("조선·선원", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),
    PRODUCTION_OTHER("생산·건설·노무 기타", IndustryCategory.PRODUCTION_CONSTRUCTION_LABOR),

    // 운전·배달 하위 카테고리
    DELIVERY_TOTAL("운전·배달 전체", IndustryCategory.DRIVING_DELIVERY),
    DELIVERY_DRIVER("납품기사", IndustryCategory.DRIVING_DELIVERY),
    SUBSTITUTE_DRIVER("대리·수행기사", IndustryCategory.DRIVING_DELIVERY),
    FOOD_DELIVERY("배달대행·음식배달", IndustryCategory.DRIVING_DELIVERY),
    HEAVY_EQUIPMENT("중장비·특수차", IndustryCategory.DRIVING_DELIVERY),
    BUS_TAXI("버스·택시·승합차", IndustryCategory.DRIVING_DELIVERY),
    WALKING_DELIVERY("도보배달", IndustryCategory.DRIVING_DELIVERY),
    QUICK_SERVICE("퀵서비스", IndustryCategory.DRIVING_DELIVERY),
    LOCATION_BASED("지입·차량용역", IndustryCategory.DRIVING_DELIVERY),
    DRIVING_OTHER("운전·배달 기타", IndustryCategory.DRIVING_DELIVERY),

    // 병원·간호·연구 하위 카테고리
    HOSPITAL_NURSE_RESEARCH("병원·간호·연구 전체", IndustryCategory.HOSPITAL_NURSING_RESEARCH),
    NURSE_CARE("간호·요양보호사", IndustryCategory.HOSPITAL_NURSING_RESEARCH),
    CLINICAL_RESEARCH("실험·연구보조", IndustryCategory.HOSPITAL_NURSING_RESEARCH),
    MEDICAL_TECHNICIAN("의료기사", IndustryCategory.HOSPITAL_NURSING_RESEARCH),
    COORDINATOR("간호조무사·간호사", IndustryCategory.HOSPITAL_NURSING_RESEARCH),
    HOSPITAL_COORDINATOR("원무·코디네이터", IndustryCategory.HOSPITAL_NURSING_RESEARCH),
    LIFE_HEALTH("생동성·임상시험", IndustryCategory.HOSPITAL_NURSING_RESEARCH),
    HOSPITAL_OTHER("병원·간호·연구 기타", IndustryCategory.HOSPITAL_NURSING_RESEARCH);

    private final String description;
    private final IndustryCategory mainCategory;

    IndustrySubcategory(String description, IndustryCategory mainCategory) {
        this.description = description;
        this.mainCategory = mainCategory;
    }

    public String getDescription() {
        return description;
    }

    public IndustryCategory getMainCategory() {
        return mainCategory;
    }
} 