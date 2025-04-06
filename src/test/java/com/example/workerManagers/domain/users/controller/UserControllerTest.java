package com.example.workerManagers.domain.users.controller;

import com.example.workerManagers.config.TestSecurityConfig;
import com.example.workerManagers.domain.users.dto.LoginRequestDto;
import com.example.workerManagers.domain.users.dto.LoginResponseDto;
import com.example.workerManagers.domain.users.dto.SignupRequestDto;
import com.example.workerManagers.domain.users.dto.SignupResponseDto;
import com.example.workerManagers.domain.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class UserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupSuccess() throws Exception {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUserEmail("test@example.com");
        requestDto.setPassword("password123!");
        requestDto.setUserName("테스트 사용자");

        SignupResponseDto responseDto = SignupResponseDto.builder()
                .userId(1L)
                .userEmail("test@example.com")
                .userName("테스트 사용자")
                .build();

        given(userService.signup(any(SignupRequestDto.class))).willReturn(responseDto);

        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"))
                .andExpect(jsonPath("$.userName").value("테스트 사용자"))
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("회원가입 테스트 성공: 사용자 이메일={}, 이름={}", requestDto.getUserEmail(), requestDto.getUserName());
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 잘못된 이메일 형식")
    void signupFailWithInvalidEmail() throws Exception {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUserEmail("invalid-email");
        requestDto.setPassword("password123!");
        requestDto.setUserName("테스트 사용자");

        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("회원가입 실패 테스트 성공: 잘못된 이메일 형식 검증 완료");
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 빈 비밀번호")
    void signupFailWithEmptyPassword() throws Exception {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUserEmail("test@example.com");
        requestDto.setPassword("");
        requestDto.setUserName("테스트 사용자");

        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("회원가입 실패 테스트 성공: 빈 비밀번호 검증 완료");
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 빈 사용자 이름")
    void signupFailWithEmptyUserName() throws Exception {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUserEmail("test@example.com");
        requestDto.setPassword("password123!");
        requestDto.setUserName("");

        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("회원가입 실패 테스트 성공: 빈 사용자 이름 검증 완료");
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setUserEmail("test@example.com");
        requestDto.setPassword("password123!");

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .accessToken("test-token")
                .tokenType("Bearer")
                .userId(1L)
                .userName("테스트 사용자")
                .build();

        given(userService.login(any(LoginRequestDto.class))).willReturn(responseDto);

        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("test-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("테스트 사용자"))
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("로그인 테스트 성공: 사용자 이메일={}", requestDto.getUserEmail());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 이메일 형식")
    void loginFailWithInvalidEmail() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setUserEmail("invalid-email");
        requestDto.setPassword("password123!");

        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("로그인 실패 테스트 성공: 잘못된 이메일 형식 검증 완료");
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 빈 비밀번호")
    void loginFailWithEmptyPassword() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setUserEmail("test@example.com");
        requestDto.setPassword("");

        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("로그인 실패 테스트 성공: 빈 비밀번호 검증 완료");
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    void logoutSuccess() throws Exception {
        // given
        String testToken = "test-token";
        doNothing().when(userService).logout(testToken);

        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그아웃이 완료되었습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("로그아웃 테스트 성공");
    }

    @Test
    @DisplayName("로그아웃 실패 테스트 - 인증되지 않은 사용자")
    void logoutFailWithUnauthorized() throws Exception {
        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("인증 토큰이 필요합니다."))
                .andExpect(jsonPath("$.success").value(false))
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("로그아웃 실패 테스트 성공: 인증되지 않은 사용자 검증 완료");
    }

    @Test
    @DisplayName("로그아웃 실패 테스트 - 유효하지 않은 토큰")
    void logoutFailWithInvalidToken() throws Exception {
        // given
        String invalidToken = "invalid-token";
        doThrow(new RuntimeException("유효하지 않은 토큰입니다."))
                .when(userService).logout(invalidToken);

        // when & then
        ResultActions resultActions = mockMvc.perform(post("/users/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("유효하지 않은 토큰입니다."))
                .andExpect(jsonPath("$.success").value(false))
                .andDo(MockMvcResultHandlers.print());
        
        // 테스트 성공 메시지 출력
        logger.info("로그아웃 실패 테스트 성공: 유효하지 않은 토큰 검증 완료");
    }
} 