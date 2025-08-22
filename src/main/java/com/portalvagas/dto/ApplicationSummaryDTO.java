package com.portalvagas.dto;

import com.portalvagas.domain.Application;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationSummaryDTO {
    private Long id;
    private String status;
    private String candidateName;
    private String candidateEmail;
    private String jobTitle;
    private String companyName;
    private LocalDateTime appliedAt;

    public ApplicationSummaryDTO() {}

    public ApplicationSummaryDTO(Application application) {
        this.id = application.getId();
        this.status = application.getStatus().name();
        this.candidateName = application.getCandidate().getFullName();
        this.candidateEmail = application.getCandidate().getUser().getEmail();
        this.jobTitle = application.getJob().getTitle();
        this.companyName = application.getJob().getCompany().getName();
        this.appliedAt = application.getAppliedAt();
    }

    public static ApplicationSummaryDTO from(Application application) {
        return application != null ? new ApplicationSummaryDTO(application) : null;
    }
}
