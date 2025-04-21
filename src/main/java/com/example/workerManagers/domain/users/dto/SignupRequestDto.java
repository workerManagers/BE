package com.example.workerManagers.domain.users.dto;

import com.example.workerManagers.domain.users.entity.User.UserType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(max = 20, message = "이름은 20자 이내로 입력해주세요.")
    private String userName;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @Size(max = 20, message = "성별은 20자 이내로 입력해주세요.")
    private String userSex;

    @Min(value = 0, message = "나이는 0보다 작을 수 없습니다.")
    private Integer userAge;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 50, message = "이메일은 50자 이내로 입력해주세요.")
    private String userEmail;

    @NotNull(message = "회원 유형은 필수 입력값입니다.")
    private UserType userType;

    // 기업 회원인 경우에만 필요한 정보
    private CompanyInfo companyInfo;

    @Getter
    @Setter
    public static class CompanyInfo {
        @NotBlank(message = "기업명은 필수 입력값입니다.")
        @Size(max = 100, message = "기업명은 100자 이내로 입력해주세요.")
        private String companyName;

        @NotBlank(message = "기업 지역은 필수 입력값입니다.")
        @Size(max = 50, message = "기업 지역은 50자 이내로 입력해주세요.")
        private String companyRegion;

        @Size(max = 20, message = "기업 코드는 20자 이내로 입력해주세요.")
        private String companyCode;
    }
} 