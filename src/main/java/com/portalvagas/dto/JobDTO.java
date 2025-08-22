package com.portalvagas.dto;

import com.portalvagas.domain.Job;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String location;
    private Boolean remote;
    private String status;
    private CompanyDTO company;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobDTO() {}

    public JobDTO(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.requirements = job.getRequirements();
        this.salaryMin = job.getSalaryMin();
        this.salaryMax = job.getSalaryMax();
        this.location = job.getLocation();
        this.remote = job.getRemote();
        this.status = job.getStatus().name();
        this.company = CompanyDTO.from(job.getCompany());
        this.createdAt = job.getCreatedAt();
        this.updatedAt = job.getUpdatedAt();
    }

    public static JobDTO from(Job job) {
        return job != null ? new JobDTO(job) : null;
    }
}
