package com.example.workerManagers.domain.restperiod.service;

import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.company.repository.CompanyRepository;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.repository.UserRepository;
import com.example.workerManagers.domain.users.exception.UserException;
import com.example.workerManagers.domain.restperiod.dto.RestPeriodRequestDto;
import com.example.workerManagers.domain.restperiod.dto.RestPeriodResponseDto;
import com.example.workerManagers.domain.restperiod.entity.RestPeriod;
import com.example.workerManagers.domain.restperiod.repository.RestPeriodRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestPeriodServiceImpl implements RestPeriodService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RestPeriodRepository restPeriodRepository;

    @Value("${fastapi.server.url}")
    private String fastApiServerUrl;

    @Override
    @Transactional
    public RestPeriodResponseDto predictRestPeriod(RestPeriodRequestDto requestDto, String userEmail) {
        log.info("휴식기간 예측 요청 시작 - 사용자 이메일: {}, 요청 데이터: {}", userEmail, requestDto);

        // 사용자 확인 및 권한 검증
        log.info("사용자 정보 조회 시작 - 이메일: {}", userEmail);
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다 - 이메일: {}", userEmail);
                    return new UserException("사용자를 찾을 수 없습니다.");
                });
        log.info("사용자 정보 조회 완료 - 사용자 ID: {}, 타입: {}", user.getUserId(), user.getUserType());

        // 회사 정보 조회
        log.info("회사 정보 조회 시작 - 사용자 ID: {}", user.getUserId());
        Company company = companyRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> {
                    log.error("회사 정보를 찾을 수 없습니다 - 사용자 ID: {}", user.getUserId());
                    return new UserException("회사 사용자만 이용할 수 있는 서비스입니다.");
                });
        log.info("회사 정보 조회 완료 - 회사 ID: {}, 회사명: {}", company.getCompanyId(), company.getCompanyName());

        try {
            // FastAPI 서버로 예측 요청
            log.info("FastAPI 서버 예측 요청 시작");
            PredictionResponse response = webClient.post()
                    .uri(fastApiServerUrl + "/predict")
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(PredictionResponse.class)
                    .block();

            if (response == null) {
                log.error("FastAPI 서버로부터 응답을 받지 못했습니다.");
                throw new RuntimeException("FastAPI 서버로부터 응답을 받지 못했습니다.");
            }

            log.info("FastAPI 서버 응답 수신 - 예측값: {}", response.getPredictedValue());

            // 예측 결과를 데이터베이스에 저장
            log.info("예측 결과 데이터베이스 저장 시작");
            RestPeriod restPeriod = RestPeriod.builder()
                    .disease(requestDto.getDisease())
                    .sex(requestDto.getSex())
                    .surgery(requestDto.getSurgery())
                    .age(requestDto.getAge())
                    .region(requestDto.getRegion())
                    .predictedPeriod(Math.round(response.getPredictedValue()))
                    .predictionDate(LocalDate.now())
                    .company(company)
                    .build();

            restPeriodRepository.save(restPeriod);
            log.info("예측 결과 데이터베이스 저장 완료 - ID: {}", restPeriod.getRestPeriod_id());

            return RestPeriodResponseDto.builder()
                    .restPeriodId(restPeriod.getRestPeriod_id())
                    .predictedValue(Math.round(response.getPredictedValue()))
                    .message("휴식기간 예측이 완료되었습니다.")
                    .build();

        } catch (Exception e) {
            log.error("휴식기간 예측 중 오류 발생: {} - 스택트레이스: {}", e.getMessage(), e);
            throw new RuntimeException("휴식기간 예측 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    private static class PredictionResponse {
        @JsonProperty("predicted_value")
        private float predictedValue;

        public float getPredictedValue() {
            return predictedValue;
        }

        public void setPredictedValue(float predictedValue) {
            this.predictedValue = predictedValue;
        }

        @Override
        public String toString() {
            return "PredictionResponse{predictedValue=" + predictedValue + '}';
        }
    }
}
