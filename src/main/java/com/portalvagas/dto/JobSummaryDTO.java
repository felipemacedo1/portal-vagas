package com.portalvagas.dto;

import com.portalvagas.domain.Job;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobSummaryDTO {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String location;
    private Boolean remote;
    private String status;
    private String companyName;
    private Boolean companyVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobSummaryDTO() {}

    public JobSummaryDTO(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.requirements = job.getRequirements();
        this.salaryMin = job.getSalaryMin();
        this.salaryMax = job.getSalaryMax();
        this.location = job.getLocation();
        this.remote = job.getRemote();
        this.status = job.getStatus().name();
        this.companyName = job.getCompany().getName();
        this.companyVerified = job.getCompany().getVerified();
        this.createdAt = job.getCreatedAt();
        this.updatedAt = job.getUpdatedAt();
    }

    public static JobSummaryDTO from(Job job) {
        return job != null ? new JobSummaryDTO(job) : null;
    }
}
