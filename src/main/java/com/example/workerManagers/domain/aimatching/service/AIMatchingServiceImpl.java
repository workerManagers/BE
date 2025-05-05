package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.AIMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.AIMatchingResponseDto;
import com.example.workerManagers.domain.aimatching.dto.JobPostMatchingDto;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.resume.entity.Resume;
import com.example.workerManagers.domain.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AIMatchingServiceImpl implements AIMatchingService {

    private final JobPostRepository jobPostRepository;
    private final ResumeRepository resumeRepository;
    private final WebClient webClient;

    @Override
    @Transactional
    public CompletableFuture<List<JobPostMatchingDto>> getMatchingScoresForAllJobPosts(String resumeText) {
        List<JobPost> jobPosts = jobPostRepository.findAll();
        
        List<CompletableFuture<JobPostMatchingDto>> futures = jobPosts.stream()
                .map(jobPost -> CompletableFuture.supplyAsync(() -> {
                    Double matchingScore = getMatchingScore(
                        createJobPostText(jobPost),
                        resumeText
                    );
                    return JobPostMatchingDto.builder()
                            .jobPostId(jobPost.getJobPostId())
                            .companyName(jobPost.getCompany().getCompanyName())
                            .jobName(jobPost.getJobCode().getJobName())
                            .jobPostDescription(jobPost.getJobPostDescription())
                            .matchingScore(matchingScore)
                            .build();
                }))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .sorted((a, b) -> b.getMatchingScore().compareTo(a.getMatchingScore()))
                        .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public CompletableFuture<List<AIMatchingResponseDto>> getMatchingScores(AIMatchingRequestDto requestDto) {
        try {
            // 이력서와 채용공고 정보를 트랜잭션 내에서 미리 로드
            Resume resume = resumeRepository.findById(requestDto.getResumeId())
                    .orElseThrow(() -> new IllegalArgumentException("Resume not found with ID: " + requestDto.getResumeId()));
            
            List<JobPost> jobPosts;
            if (requestDto.getJobPostId() != null) {
                // 단일 채용공고에 대한 매칭
                JobPost jobPost = jobPostRepository.findById(requestDto.getJobPostId())
                        .orElseThrow(() -> new IllegalArgumentException("Job post not found with ID: " + requestDto.getJobPostId()));
                jobPosts = Collections.singletonList(jobPost);
            } else {
                // 모든 채용공고에 대한 매칭
                jobPosts = jobPostRepository.findAll();
            }
            
            // 필요한 데이터를 미리 추출
            String resumeText = resume.getResumeText();
            List<JobPostData> jobPostDataList = jobPosts.stream()
                    .map(jobPost -> new JobPostData(
                        jobPost.getJobPostId(),
                        jobPost.getCompany().getCompanyName(),
                        jobPost.getJobCode().getJobName(),
                        createJobPostText(jobPost)))
                    .collect(Collectors.toList());

            // 트랜잭션 외부에서 비동기 처리
            return CompletableFuture.supplyAsync(() -> 
                jobPostDataList.stream()
                    .map(jobPostData -> {
                        Double matchingScore = getMatchingScore(
                            jobPostData.jobPostText,
                            resumeText
                        );
                        
                        return AIMatchingResponseDto.builder()
                                .jobPostId(jobPostData.jobPostId)
                                .companyName(jobPostData.companyName)
                                .jobName(jobPostData.jobName)
                                .matchingScore(matchingScore)
                                .build();
                    })
                    .sorted((a, b) -> Double.compare(b.getMatchingScore(), a.getMatchingScore()))
                    .collect(Collectors.toList())
            );
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    // JobPostData 내부 클래스 추가
    @lombok.Value
    private static class JobPostData {
        Long jobPostId;
        String companyName;
        String jobName;
        String jobPostText;
    }

    private String createJobPostText(JobPost jobPost) {
        return String.format(
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
    }

    private Double getMatchingScore(String jobPostText, String resumeText) {
        return webClient.post()
                .uri("/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFastApiRequest(jobPostText, resumeText))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                        "FastAPI 서버 에러: " + errorBody))))
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);
                        
                        // task_id가 있는 경우 상태 확인
                        if (root.has("task_id")) {
                            String taskId = root.get("task_id").asText();
                            return checkTaskStatus(taskId);
                        }
                        
                        // 직접적인 결과인 경우
                        return Mono.just(parseMatchingResponse(response));
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Failed to parse FastAPI response: " + e.getMessage()));
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .block();
    }

    private Mono<Double> checkTaskStatus(String taskId) {
        return webClient.get()
                .uri("/compare/status/" + taskId)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);
                        
                        if (root.has("status")) {
                            String status = root.get("status").asText();
                            
                            if ("completed".equals(status) && root.has("result")) {
                                return Mono.just(parseMatchingResponse(root.get("result").toString()));
                            } else if ("processing".equals(status)) {
                                return Mono.error(new RuntimeException("Task is still processing"));
                            } else {
                                return Mono.error(new RuntimeException("Task failed or unknown status: " + status));
                            }
                        }
                        return Mono.error(new RuntimeException("Invalid status response format"));
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Failed to parse status response: " + e.getMessage()));
                    }
                })
                .retryWhen(Retry.fixedDelay(30, Duration.ofSeconds(2))
                        .filter(ex -> ex instanceof RuntimeException)
                        .filter(ex -> ex.getMessage().contains("Task is still processing")));
    }

    private Double parseMatchingResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            if (root.isArray() && root.size() > 0) {
                JsonNode firstItem = root.get(0);
                if (firstItem.has("similarity")) {
                    String similarityStr = firstItem.get("similarity").asText();
                    return Double.parseDouble(similarityStr.replace("점", ""));
                }
            }
            throw new RuntimeException("Invalid response format: " + response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse matching result: " + e.getMessage());
        }
    }

    private Object createFastApiRequest(String jobPostText, String resumeText) {
        return new Object() {
            public final String input_text = jobPostText;
            public final List<Object> dataset = List.of(
                new Object() {
                    public final Long resume_id = 1L;
                    public final String resume_description = resumeText;
                }
            );
        };
    }
} 