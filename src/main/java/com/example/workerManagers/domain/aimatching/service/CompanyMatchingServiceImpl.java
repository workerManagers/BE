package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.CompanyMatchingResponseDto;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.resume.entity.Resume;
import com.example.workerManagers.domain.resume.repository.ResumeRepository;
import com.example.workerManagers.domain.application.entity.Application;
import com.example.workerManagers.domain.application.repository.ApplicationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyMatchingServiceImpl implements CompanyMatchingService {

    private final JobPostRepository jobPostRepository;
    private final ApplicationRepository applicationRepository;
    private final ResumeRepository resumeRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${fastapi.url:https://port-0-workermanagers-ai-m9i2iiuc1e546d59.sel4.cloudtype.app}")
    private String fastApiUrl;

    @Override
    public CompletableFuture<List<CompanyMatchingResponseDto>> getMatchingScoresForResumes(CompanyMatchingRequestDto requestDto) {
        return CompletableFuture.supplyAsync(() -> {
            // 채용 공고 정보 가져오기
            JobPost jobPost = jobPostRepository.findById(requestDto.getJobPostId())
                    .orElseThrow(() -> new IllegalArgumentException("Job post not found"));

            // 매칭이 허용된 모든 이력서 가져오기
            List<Resume> matchingEnabledResumes = resumeRepository.findAllByMatchingEnabled();

            // 각 이력서와 매칭 점수 계산
            List<CompletableFuture<CompanyMatchingResponseDto>> futures = matchingEnabledResumes.stream()
                    .map(resume -> CompletableFuture.supplyAsync(() -> {
                        Double matchingScore = getMatchingScore(
                            createJobPostText(jobPost),
                            resume.getResumeText()
                        );
                        
                        return CompanyMatchingResponseDto.builder()
                                .resumeId(resume.getResumeId())
                                .applicantName(resume.getUser().getUserName())
                                .matchingScore(matchingScore)
                                .resumeText(resume.getResumeText())
                                .build();
                    }))
                    .collect(Collectors.toList());

            // 모든 비동기 작업 완료 대기
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> futures.stream()
                            .map(CompletableFuture::join)
                            .sorted((a, b) -> Double.compare(b.getMatchingScore(), a.getMatchingScore()))
                            .collect(Collectors.toList()))
                    .join();
        });
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
        // 초기 요청을 보내고 task_id를 받음
        String taskId = webClient.post()
                .uri("/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFastApiRequest(jobPostText, resumeText))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode root = objectMapper.readTree(response);
                        return root.get("task_id").asText();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse task_id from response: " + response);
                    }
                })
                .block();

        // task_id로 결과를 폴링
        return webClient.get()
                .uri("/compare/status/" + taskId)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode root = objectMapper.readTree(response);
                        if (root.has("status") && "completed".equals(root.get("status").asText())) {
                            JsonNode result = root.get("result");
                            if (result.isArray() && result.size() > 0) {
                                JsonNode firstItem = result.get(0);
                                if (firstItem.has("similarity")) {
                                    String similarityStr = firstItem.get("similarity").asText();
                                    return Double.parseDouble(similarityStr.replace("점", ""));
                                }
                            }
                        }
                        throw new RuntimeException("Task not completed or invalid response format");
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse matching result: " + response);
                    }
                })
                .retryWhen(Retry.fixedDelay(30, Duration.ofSeconds(2))  // 2초 간격으로 30번 재시도
                        .filter(ex -> ex instanceof RuntimeException)
                        .filter(ex -> ex.getMessage().contains("Task not completed")))
                .block();
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