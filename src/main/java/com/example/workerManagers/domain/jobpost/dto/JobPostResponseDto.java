package com.example.workerManagers.domain.jobpost.dto;

import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.entity.CareerType;
import com.example.workerManagers.domain.jobpost.entity.RecruitmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostResponseDto {
    private Long jobPostId;
    private Long authorId;
    private String authorName;
    private String authorEmail;
    private Long companyId;
    private String companyName;
    private Long jobCodeId;
    private String jobCodeName;
    private String jobName;
    private String jobPostDescription;
    private String mainTasks;
    private String qualifications;
    private String preferredQualifications;
    private String idealCandidate;
    private String jobPeriod;
    private String jobRegion;
    private LocalDateTime deadline;
    private CareerType careerType;
    private RecruitmentStatus recruitmentStatus;
    private String message;

    @Builder
    public JobPostResponseDto(Long jobPostId, String jobName, Long companyId, String companyName,
                            Long jobCodeId, String jobCodeName, String jobPostDescription,
                            String jobPeriod, LocalDateTime deadline, CareerType careerType,
                            String mainTasks, String qualifications, String preferredQualifications,
                            String idealCandidate, String jobRegion, RecruitmentStatus recruitmentStatus,
                            String message) {
        this.jobPostId = jobPostId;
        this.jobName = jobName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.jobCodeId = jobCodeId;
        this.jobCodeName = jobCodeName;
        this.jobPostDescription = jobPostDescription;
        this.jobPeriod = jobPeriod;
        this.deadline = deadline;
        this.careerType = careerType;
        this.mainTasks = mainTasks;
        this.qualifications = qualifications;
        this.preferredQualifications = preferredQualifications;
        this.idealCandidate = idealCandidate;
        this.jobRegion = jobRegion;
        this.recruitmentStatus = recruitmentStatus;
        this.message = message;
    }

    public static JobPostResponseDto of(JobPost jobPost, String message) {
        return JobPostResponseDto.builder()
                .jobPostId(jobPost.getJobPostId())
                .authorId(jobPost.getCompany().getUser().getUserId())
                .authorName(jobPost.getCompany().getUser().getUserName())
                .authorEmail(jobPost.getCompany().getUser().getUserEmail())
                .companyId(jobPost.getCompany().getCompanyId())
                .companyName(jobPost.getCompany().getCompanyName())
                .jobCodeId(jobPost.getJobCode().getJobCodeId())
                .jobCodeName(jobPost.getJobCode().getJobName())
                .jobName(jobPost.getJobCode().getJobName())
                .jobPostDescription(jobPost.getJobPostDescription())
                .mainTasks(jobPost.getMainTasks())
                .qualifications(jobPost.getQualifications())
                .preferredQualifications(jobPost.getPreferredQualifications())
                .idealCandidate(jobPost.getIdealCandidate())
                .jobPeriod(jobPost.getJobPeriod())
                .jobRegion(jobPost.getJobRegion())
                .deadline(jobPost.getDeadline())
                .careerType(jobPost.getCareerType())
                .recruitmentStatus(jobPost.getRecruitmentStatus())
                .message(message)
                .build();
    }

    public static JobPostResponseDto from(JobPost jobPost) {
        return JobPostResponseDto.builder()
                .jobPostId(jobPost.getJobPostId())
                .authorId(jobPost.getCompany().getUser().getUserId())
                .authorName(jobPost.getCompany().getUser().getUserName())
                .authorEmail(jobPost.getCompany().getUser().getUserEmail())
                .companyName(jobPost.getCompany().getCompanyName())
                .jobName(jobPost.getJobName())
                .jobPostDescription(jobPost.getJobPostDescription())
                .mainTasks(jobPost.getMainTasks())
                .qualifications(jobPost.getQualifications())
                .preferredQualifications(jobPost.getPreferredQualifications())
                .idealCandidate(jobPost.getIdealCandidate())
                .jobPeriod(jobPost.getJobPeriod())
                .jobRegion(jobPost.getJobRegion())
                .deadline(jobPost.getDeadline())
                .careerType(jobPost.getCareerType())
                .recruitmentStatus(jobPost.getRecruitmentStatus())
                .build();
    }
}