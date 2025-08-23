package com.portalvagas.dto;

import com.portalvagas.domain.Job;
import java.time.LocalDateTime;

public record JobDto(
    Long id,
    String title,
    String description,
    String requirements,
    String location,
    Boolean remote,
    Double salaryMin,
    Double salaryMax,
    String type,
    Job.Status status,
    CompanyDto company,
    Integer applicationsCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public record CompanyDto(
        Long id,
        String name,
        String description,
        String website,
        Boolean verified
    ) {}
}