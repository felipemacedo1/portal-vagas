package com.portalvagas.dto;

import com.portalvagas.domain.Company;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyDTO {
    private Long id;
    private String name;
    private String description;
    private String website;
    private Boolean verified;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CompanyDTO() {}

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.description = company.getDescription();
        this.website = company.getWebsite();
        this.verified = company.getVerified();
        this.user = UserDTO.from(company.getUser());
        this.createdAt = company.getCreatedAt();
        this.updatedAt = company.getUpdatedAt();
    }

    public static CompanyDTO from(Company company) {
        return company != null ? new CompanyDTO(company) : null;
    }
}
