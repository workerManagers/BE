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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AIMatchingServiceImpl implements AIMatchingService {

    private final JobPostRepository jobPostRepository;
    private final ResumeRepository resumeRepository;
    private final WebClient webClient;

    @Override
    public List<JobPostMatchingDto> getMatchingScoresForAllJobPosts(String resumeText) {
        List<JobPost> jobPosts = jobPostRepository.findAll();
        return jobPosts.stream()
                .map(jobPost -> {
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
                })
                .sorted((a, b) -> b.getMatchingScore().compareTo(a.getMatchingScore()))
                .collect(Collectors.toList());
    }

    @Override
    public CompletableFuture<List<AIMatchingResponseDto>> getMatchingScores(AIMatchingRequestDto requestDto) {
        // 이력서 조회
        Resume resume = resumeRepository.findById(requestDto.getResumeId())
                .orElseThrow(() -> new IllegalArgumentException("Resume not found"));

        // 모든 채용 공고 조회
        List<JobPost> jobPosts = jobPostRepository.findAll();

        // 비동기로 매칭 점수 계산
        List<CompletableFuture<AIMatchingResponseDto>> futures = jobPosts.stream()
                .map(jobPost -> CompletableFuture.supplyAsync(() -> {
                    Double matchingScore = getMatchingScore(
                        createJobPostText(jobPost),
                        resume.getResumeText()
                    );
                    
                    return AIMatchingResponseDto.builder()
                            .jobPostId(jobPost.getJobPostId())
                            .companyName(jobPost.getCompany().getCompanyName())
                            .jobName(jobPost.getJobCode().getJobName())
                            .matchingScore(matchingScore)
                            .build();
                }))
                .collect(Collectors.toList());

        // 모든 비동기 작업 완료 대기
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .sorted((a, b) -> Double.compare(b.getMatchingScore(), a.getMatchingScore()))
                        .collect(Collectors.toList()));
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
                .map(this::parseMatchingResponse)
                .subscribeOn(Schedulers.boundedElastic())
                .block();
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
            throw new RuntimeException("Failed to parse FastAPI response: " + e.getMessage());
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