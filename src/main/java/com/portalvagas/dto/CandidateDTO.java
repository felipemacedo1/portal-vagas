package com.portalvagas.dto;

import com.portalvagas.domain.Candidate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CandidateDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String cvFilename;
    private String cvContentType;
    private boolean hasCv;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CandidateDTO() {}

    public CandidateDTO(Candidate candidate) {
        this.id = candidate.getId();
        this.email = candidate.getUser() != null ? candidate.getUser().getEmail() : null;
        this.fullName = candidate.getFullName();
        this.phone = candidate.getPhone();
        this.cvFilename = candidate.getCvFilename();
        this.cvContentType = candidate.getCvContentType();
        this.hasCv = candidate.getCvData() != null;
        this.user = UserDTO.from(candidate.getUser());
        this.createdAt = candidate.getCreatedAt();
        this.updatedAt = candidate.getUpdatedAt();
    }

    public static CandidateDTO from(Candidate candidate) {
        return candidate != null ? new CandidateDTO(candidate) : null;
    }
}
