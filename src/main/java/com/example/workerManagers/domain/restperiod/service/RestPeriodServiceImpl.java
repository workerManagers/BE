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
    public RestPeriodResponseDto predictRestPeriod(RestPeriodRequestDto requestDto) {
        log.info("휴식기간 예측 요청: {}", requestDto);

        // 사용자 확인 및 권한 검증
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));

        if (!"COMPANY".equals(user.getUserType())) {
            throw new UserException("회사 사용자만 이용할 수 있는 서비스입니다.");
        }

        // 회사 정보 조회
        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("회사 정보를 찾을 수 없습니다."));

        try {
            // FastAPI 서버로 예측 요청
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

            log.info("FastAPI 서버 응답: {}", response);

            // 예측 결과를 데이터베이스에 저장
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
            log.info("예측 결과가 데이터베이스에 저장되었습니다: {}", restPeriod);

            return RestPeriodResponseDto.builder()
                    .restPeriodId(restPeriod.getRestPeriod_id())
                    .predictedValue(Math.round(response.getPredictedValue()))
                    .message("휴식기간 예측이 완료되었습니다.")
                    .build();

        } catch (Exception e) {
            log.error("휴식기간 예측 중 오류 발생: {}", e.getMessage(), e);
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
