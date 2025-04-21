package com.example.workerManagers.domain.aimatching.service;

import com.example.workerManagers.domain.aimatching.dto.AIMatchingRequestDto;
import com.example.workerManagers.domain.aimatching.dto.JobPostMatchingDto;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AIMatchingServiceImpl implements AIMatchingService {

    private final JobPostRepository jobPostRepository;
    private final WebClient webClient;

    @Value("${fastapi.url:https://port-0-workermanagers-ai-m9i2iiuc1e546d59.sel4.cloudtype.app}")
    private String fastApiUrl;

    @Override
    public List<JobPostMatchingDto> getMatchingScoresForAllJobPosts(String resumeText) {
        List<JobPost> jobPosts = jobPostRepository.findAll();
        return jobPosts.stream()
                .map(jobPost -> {
                    AIMatchingRequestDto requestDto = convertJobPostToFastApiRequest(jobPost, resumeText);
                    Double matchingScore = getMatchingScore(requestDto);
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MatchingScoreResponse {
        private Double score;
        private String message;
    }

    private Double getMatchingScore(AIMatchingRequestDto requestDto) {
        return webClient.post()
                .uri(fastApiUrl + "/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);
                        if (root.isArray() && root.size() > 0) {
                            String similarity = root.get(0).get("similarity").asText();
                            // "76.62점"에서 숫자만 추출
                            return Double.parseDouble(similarity.replaceAll("[^0-9.]", ""));
                        }
                        throw new RuntimeException("Invalid response format");
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse FastAPI response", e);
                    }
                })
                .block();
    }

    private AIMatchingRequestDto convertJobPostToFastApiRequest(JobPost jobPost, String resumeText) {
        String jobPostDescription = String.format(
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

        AIMatchingRequestDto.JobPostData jobPostData = new AIMatchingRequestDto.JobPostData();
        jobPostData.setJobPost_id(jobPost.getJobPostId());
        jobPostData.setJobPost_description(jobPostDescription);

        AIMatchingRequestDto requestDto = new AIMatchingRequestDto();
        requestDto.setInput_text(resumeText);
        requestDto.setDataset(Collections.singletonList(jobPostData));

        return requestDto;
    }
} 