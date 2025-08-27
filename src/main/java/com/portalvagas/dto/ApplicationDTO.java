package com.portalvagas.dto;

import com.portalvagas.domain.Application;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationDTO {
    private Long id;
    private String status;
    private Long candidateId;
    private CandidateDTO candidate;
    private Long jobId;
    private JobDTO job;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    public ApplicationDTO() {}

    public ApplicationDTO(Application application) {
        this.id = application.getId();
        this.status = mapStatus(application.getStatus());
        this.candidateId = application.getCandidate() != null ? application.getCandidate().getId() : null;
        this.candidate = CandidateDTO.from(application.getCandidate());
        this.jobId = application.getJob() != null ? application.getJob().getId() : null;
        this.job = JobDTO.from(application.getJob());
        this.appliedAt = application.getAppliedAt();
        this.updatedAt = application.getAppliedAt();
    }

    public static ApplicationDTO from(Application application) {
        return application != null ? new ApplicationDTO(application) : null;
    }

    private String mapStatus(Application.Status status) {
        return switch (status) {
            case ACCEPTED -> "APPROVED";
            case REJECTED -> "REJECTED";
            case REVIEWED -> "INTERVIEW";
            case PENDING -> "PENDING";
        };
    }
}
