package com.example.workerManagers.domain.restperiod.service;

import com.example.workerManagers.domain.restperiod.dto.RestPeriodRequestDto;
import com.example.workerManagers.domain.restperiod.dto.RestPeriodResponseDto;
import com.example.workerManagers.domain.restperiod.entity.RestPeriod;
import com.example.workerManagers.domain.restperiod.repository.RestPeriodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestPeriodServiceImpl implements RestPeriodService {

    private final WebClient webClient;
    private final RestPeriodRepository restPeriodRepository;

    @Value("${fastapi.server.url}")
    private String fastApiServerUrl;

    @Override
    @Transactional
    public RestPeriodResponseDto predictRestPeriod(RestPeriodRequestDto requestDto) {
        log.info("휴식기간 예측 요청: {}", requestDto);

        try {
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
                    .predictedPeriod(response.getPredictedPeriod())
                    .predictionDate(LocalDate.now())
                    .build();

            restPeriodRepository.save(restPeriod);
            log.info("예측 결과가 데이터베이스에 저장되었습니다: {}", restPeriod);

            return RestPeriodResponseDto.builder()
                    .predictedPeriod(response.getPredictedPeriod())
                    .build();

        } catch (Exception e) {
            log.error("휴식기간 예측 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("휴식기간 예측 중 오류가 발생했습니다.", e);
        }
    }

    private static class PredictionResponse {
        private int predictedPeriod;

        public int getPredictedPeriod() {
            return predictedPeriod;
        }

        public void setPredictedPeriod(int predictedPeriod) {
            this.predictedPeriod = predictedPeriod;
        }

        @Override
        public String toString() {
            return "PredictionResponse{" +
                    "predictedPeriod=" + predictedPeriod +
                    '}';
        }
    }
}
