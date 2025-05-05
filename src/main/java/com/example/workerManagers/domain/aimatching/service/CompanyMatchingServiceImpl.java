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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyMatchingServiceImpl implements CompanyMatchingService {

    private final JobPostRepository jobPostRepository;
    private final ApplicationRepository applicationRepository;
    private final ResumeRepository resumeRepository;
    private final WebClient webClient;

    @Value("${fastapi.url:https://port-0-workermanagers-ai-m9i2iiuc1e546d59.sel4.cloudtype.app}")
    private String fastApiUrl;

    @Override
    public List<CompanyMatchingResponseDto> getMatchingScoresForResumes(CompanyMatchingRequestDto requestDto) {
        // 채용 공고 정보 가져오기
        JobPost jobPost = jobPostRepository.findById(requestDto.getJobPostId())
                .orElseThrow(() -> new IllegalArgumentException("Job post not found"));

        // 매칭이 허용된 모든 이력서 가져오기
        List<Resume> matchingEnabledResumes = resumeRepository.findAllByMatchingEnabled();

        // 각 이력서와 매칭 점수 계산
        return matchingEnabledResumes.stream()
                .map(resume -> {
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
                })
                .sorted((a, b) -> Double.compare(b.getMatchingScore(), a.getMatchingScore()))
                .collect(Collectors.toList());
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

    private Object createFastApiRequest(String jobPostText, String resumeText) {
        return new Object() {
            public final String input_text = jobPostText;
            public final List<Object> dataset = List.of(
                new Object() {
                    public final Long resume_id = 1L; // 임시 ID
                    public final String resume_description = resumeText;
                }
            );
        };
    }

    private Double getMatchingScore(String jobPostText, String resumeText) {
        return webClient.post()
                .uri(fastApiUrl + "/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFastApiRequest(jobPostText, resumeText))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> {
                    return response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException(
                                    "FastAPI 서버 에러: " + errorBody)));
                })
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);
                        if (root.isArray() && root.size() > 0) {
                            JsonNode firstItem = root.get(0);
                            if (firstItem.has("similarity")) {
                                String similarityStr = firstItem.get("similarity").asText();
                                // "점" 문자 제거하고 숫자로 변환
                                return Double.parseDouble(similarityStr.replace("점", ""));
                            }
                        }
                        throw new RuntimeException("Invalid response format: " + response);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse FastAPI response: " + e.getMessage());
                    }
                })
                .block();
    }
} 