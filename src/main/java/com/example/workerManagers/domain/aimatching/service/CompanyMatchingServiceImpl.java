package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingResponseDto;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.resume.entity.Resume;
import com.example.workerManagers.domain.resume.repository.ResumeRepository;
import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.application.repository.ApplicationRepository;
import com.example.workerManagers.domain.company.entity.Company;
import com.example.workerManagers.domain.jobcode.entity.JobCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyMatchingServiceImpl implements CompanyMatchingService {

    private final JobPostRepository jobPostRepository;
    private final ApplicationRepository applicationRepository;
    private final ResumeRepository resumeRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    @Qualifier("taskExecutor")
    private final Executor taskExecutor;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${fastapi.url:https://port-0-workermanagers-ai-m9i2iiuc1e546d59.sel4.cloudtype.app}")
    private String fastApiUrl;

    @Override
    @Transactional(readOnly = true)
    public CompletableFuture<List<CompanyMatchingResponseDto>> getMatchingScoresForResumes(CompanyMatchingRequestDto requestDto) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        
        log.debug("Current authentication in main thread: {}", authentication);

        return CompletableFuture.supplyAsync(() -> {
            try {
                SecurityContextHolder.setContext(securityContext);
                log.debug("Set authentication in async thread: {}", SecurityContextHolder.getContext().getAuthentication());

                // Fetch JobPost with eager loading
                JobPost jobPost = jobPostRepository.findByIdWithRelationships(requestDto.getJobPostId())
                        .orElseThrow(() -> new IllegalArgumentException("Job post not found"));
                
                log.debug("Found job post: {} with company: {} and job code: {}", 
                    jobPost.getJobPostId(), 
                    jobPost.getCompany().getCompanyName(),
                    jobPost.getJobCode().getJobName());

                List<Resume> matchingEnabledResumes = resumeRepository.findAllByMatchingEnabled();
                log.debug("Found {} matching enabled resumes", matchingEnabledResumes.size());

                String jobPostText = createJobPostText(jobPost);

                int batchSize = 1; // 무료버전은 1개씩만!
                List<CompanyMatchingResponseDto> results = new ArrayList<>();

                for (int i = 0; i < matchingEnabledResumes.size(); i += batchSize) {
                    List<Resume> batch = matchingEnabledResumes.subList(i, Math.min(i + batchSize, matchingEnabledResumes.size()));

                    List<CompletableFuture<CompanyMatchingResponseDto>> futures = batch.stream()
                        .map(resume -> CompletableFuture.supplyAsync(() -> {
                            try {
                                SecurityContextHolder.setContext(securityContext);
                                log.debug("Processing resume: {}", resume.getResumeId());
                                
                                Double matchingScore = getMatchingScore(
                                    jobPostText,
                                    resume.getResumeText(),
                                    jobPost.getJobPostId()
                                );
                                
                                log.debug("Got matching score {} for resume {}", matchingScore, resume.getResumeId());
                                
                                return CompanyMatchingResponseDto.builder()
                                        .resumeId(resume.getResumeId())
                                        .applicantName(resume.getUser().getUserName())
                                        .matchingScore(matchingScore)
                                        .resumeText(resume.getResumeText())
                                        .build();
                            } catch (Exception e) {
                                log.error("Error processing resume {}: {}", resume.getResumeId(), e.getMessage(), e);
                                throw e;
                            } finally {
                                SecurityContextHolder.clearContext();
                            }
                        }, taskExecutor))
                        .collect(Collectors.toList());

                    // batch 내 모든 작업이 끝날 때까지 대기
                    List<CompanyMatchingResponseDto> batchResults = futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());

                    results.addAll(batchResults);

                    // batch 간 딜레이(1초)
                    try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }

                return results.stream()
                    .sorted((a, b) -> Double.compare(b.getMatchingScore(), a.getMatchingScore()))
                    .collect(Collectors.toList());
            } catch (Exception e) {
                log.error("Error in main async task: {}", e.getMessage(), e);
                throw e;
            } finally {
                SecurityContextHolder.clearContext();
            }
        }, taskExecutor);
    }

    private String createJobPostText(JobPost jobPost) {
        try {
            String text = String.format(
                "[%s - %s]\n\n%s\n\n[주요 업무]\n%s\n\n[자격 요건]\n%s\n\n[우대 사항]\n%s\n\n[인재상]\n%s\n\n[근무 기간]\n%s\n\n[근무 지역]\n%s\n\n[마감일]\n%s",
                jobPost.getCompany().getCompanyName(),
                jobPost.getJobCode().getJobName(),
                jobPost.getJobPostDescription(),
                jobPost.getMainTasks(),
                jobPost.getQualifications(),
                jobPost.getPreferredQualifications(),
                jobPost.getIdealCandidate(),
                jobPost.getJobPeriod(),
                jobPost.getJobRegion(),
                jobPost.getDeadline().toString()
            );
            log.debug("Created job post text: {}", text);
            return text;
        } catch (Exception e) {
            log.error("Error creating job post text: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Double getMatchingScore(String jobPostText, String resumeText, Long jobPostId) {
        try {
            log.debug("Getting matching score for job post text length: {}, resume text length: {}", 
                     jobPostText.length(), resumeText.length());
            
            Object requestBody = createFastApiRequest(jobPostText, resumeText, jobPostId);
            log.debug("Sending request to FastAPI server...");
            
            // 초기 요청을 보내고 task_id를 받음
            String taskId = webClient.post()
                    .uri("/compare")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(5, Duration.ofSeconds(5))  // 재시도 횟수와 대기 시간 증가
                        .maxBackoff(Duration.ofSeconds(20))
                        .filter(throwable -> {
                            if (throwable instanceof WebClientResponseException) {
                                WebClientResponseException ex = (WebClientResponseException) throwable;
                                int statusCode = ex.getStatusCode().value();
                                // 502, 503 에러의 경우 재시도
                                boolean shouldRetry = (statusCode == 502 || statusCode == 503);
                                if (shouldRetry) {
                                    log.warn("FastAPI 서버 응답 오류 (상태 코드: {}). 서버가 일시적으로 불안정한 상태입니다.", 
                                        statusCode);
                                } else {
                                    log.error("FastAPI 서버 응답 오류: 상태 코드={}, 응답={}", 
                                        statusCode, ex.getResponseBodyAsString());
                                }
                                return shouldRetry;
                            }
                            return false;
                        })
                        .doBeforeRetry(retrySignal -> {
                            long retryCount = retrySignal.totalRetries() + 1;
                            log.warn("AI 서버 응답 실패. {}번째 재시도 중... (최대 5회)", retryCount);
                            // 재시도 전에 잠시 대기하여 서버가 wake up할 시간을 줌
                            try {
                                Thread.sleep(2000);  // 2초 대기
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        })
                    )
                    .map(response -> {
                        try {
                            log.debug("Received initial response: {}", response);
                            JsonNode root = objectMapper.readTree(response);
                            if (!root.has("task_id")) {
                                log.error("Invalid response format. Response: {}", response);
                                throw new RuntimeException("AI 서버 응답에 task_id가 없습니다.");
                            }
                            return root.get("task_id").asText();
                        } catch (Exception e) {
                            log.error("AI 서버 응답 파싱 실패: {}", response, e);
                            throw new RuntimeException("AI 서버 응답을 처리할 수 없습니다.", e);
                        }
                    })
                    .block();

            if (taskId == null) {
                throw new RuntimeException("AI 서버에서 task_id를 받지 못했습니다.");
            }

            log.debug("Got task ID: {}", taskId);

            // task_id로 결과를 폴링
            return webClient.get()
                    .uri("/compare/status/" + taskId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(5, Duration.ofSeconds(5))
                        .maxBackoff(Duration.ofSeconds(20))
                        .filter(throwable -> {
                            if (throwable instanceof WebClientResponseException) {
                                WebClientResponseException ex = (WebClientResponseException) throwable;
                                int statusCode = ex.getStatusCode().value();
                                boolean shouldRetry = (statusCode == 502 || statusCode == 503) ||
                                    (throwable.getMessage() != null && 
                                     throwable.getMessage().contains("Task not completed"));
                                if (shouldRetry) {
                                    log.warn("상태 확인 응답 오류 (상태 코드: {}). 서버가 일시적으로 불안정한 상태입니다.", 
                                        statusCode);
                                } else {
                                    log.error("상태 확인 응답 오류: 상태 코드={}, 응답={}", 
                                        statusCode, ex.getResponseBodyAsString());
                                }
                                return shouldRetry;
                            }
                            return false;
                        })
                        .doBeforeRetry(retrySignal -> {
                            long retryCount = retrySignal.totalRetries() + 1;
                            log.warn("상태 확인 실패. {}번째 재시도 중... (최대 5회)", retryCount);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        })
                    )
                    .map(response -> {
                        try {
                            log.debug("Received status response: {}", response);
                            JsonNode root = objectMapper.readTree(response);
                            if (root.has("status") && "completed".equals(root.get("status").asText())) {
                                JsonNode result = root.get("result");
                                if (result.isArray() && result.size() > 0) {
                                    JsonNode firstItem = result.get(0);
                                    if (firstItem.has("similarity")) {
                                        String similarityStr = firstItem.get("similarity").asText();
                                        Double score = Double.parseDouble(similarityStr.replace("점", ""));
                                        log.debug("Parsed matching score: {}", score);
                                        return score;
                                    }
                                }
                            }
                            log.error("Invalid response format or incomplete task. Response: {}", response);
                            throw new RuntimeException("Task not completed or invalid response format");
                        } catch (Exception e) {
                            log.error("Failed to parse matching result: {}", response, e);
                            throw new RuntimeException("Failed to parse matching result: " + response, e);
                        }
                    })
                    .block();
        } catch (Exception e) {
            log.error("Error getting matching score: {}", e.getMessage(), e);
            if (e.getMessage().contains("AI 서버가 현재 사용 불가능합니다") || 
                e.getMessage().contains("502") || 
                e.getMessage().contains("503")) {
                throw new RuntimeException(
                    "AI 서버가 현재 일시적으로 불안정한 상태입니다. 잠시 후 다시 시도해주세요. " +
                    "서버가 초기화되는 데 1-2분 정도 소요될 수 있습니다."
                );
            }
            throw new RuntimeException("매칭 점수를 계산할 수 없습니다. AI 서버와의 통신 중 오류가 발생했습니다.", e);
        }
    }

    private Object createFastApiRequest(String jobPostText, String resumeText, Long jobPostId) {
        try {
            // FastAPI 요청 형식에 맞게 데이터 구성
            Map<String, Object> request = new HashMap<>();
            request.put("input_text", jobPostText);  // 채용공고 내용을 input_text로
            
            Map<String, Object> resumeData = new HashMap<>();
            resumeData.put("resume_id", "1");  // 이력서 ID
            resumeData.put("resume_description", resumeText);  // 이력서 내용
            
            request.put("dataset", Collections.singletonList(resumeData));

            // 요청 데이터 로깅
            log.debug("FastAPI 요청 데이터: {}", objectMapper.writeValueAsString(request));
            
            return request;
        } catch (Exception e) {
            log.error("FastAPI 요청 데이터 생성 중 오류: {}", e.getMessage(), e);
            throw new RuntimeException("AI 서버 요청 데이터를 생성할 수 없습니다.", e);
        }
    }
} 