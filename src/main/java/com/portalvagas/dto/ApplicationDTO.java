package com.portalvagas.dto;

import com.portalvagas.domain.Application;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationDTO {
    private Long id;
    private String status;
    private CandidateDTO candidate;
    private JobDTO job;
    private LocalDateTime appliedAt;

    public ApplicationDTO() {}

    public ApplicationDTO(Application application) {
        this.id = application.getId();
        this.status = application.getStatus().name();
        this.candidate = CandidateDTO.from(application.getCandidate());
        this.job = JobDTO.from(application.getJob());
        this.appliedAt = application.getAppliedAt();
    }

    public static ApplicationDTO from(Application application) {
        return application != null ? new ApplicationDTO(application) : null;
    }
}
