package com.example.workerManagers.domain.users.dto;

import com.example.workerManagers.domain.users.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userSex;
    private Integer userAge;
    private User.UserType userType;
    private CompanyInfo companyInfo;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyInfo {
        private String companyName;
        private String companyRegion;
        private String companyCode;
    }
} 