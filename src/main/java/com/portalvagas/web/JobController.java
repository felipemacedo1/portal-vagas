package com.portalvagas.web;

import com.portalvagas.domain.Application;
import com.portalvagas.domain.Job;
import com.portalvagas.domain.User;
import com.portalvagas.dto.ApplicationSummaryDTO;
import com.portalvagas.dto.JobSummaryDTO;
import com.portalvagas.dto.ApplicationDTO;
import com.portalvagas.service.JobService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobSummaryDTO> createJob(
            @Valid @RequestBody CreateJobRequest request,
            @AuthenticationPrincipal User user) {
        
        Job job = jobService.createJob(
                user, request.getTitle(), request.getDescription(), request.getRequirements(),
                request.getSalaryMin(), request.getSalaryMax(), request.getLocation(), request.getRemote());
        
        return ResponseEntity.ok(JobSummaryDTO.from(job));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobSummaryDTO> submitJob(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Job job = jobService.submitJob(id, user);
        return ResponseEntity.ok(JobSummaryDTO.from(job));
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public Page<JobSummaryDTO> listEmployerJobs(@AuthenticationPrincipal User user, Pageable pageable) {
        return jobService.getEmployerJobSummaries(user, pageable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobSummaryDTO> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobRequest request,
            @AuthenticationPrincipal User user) {
        Job job = jobService.updateJob(
            id, user,
            request.getTitle(), request.getDescription(), request.getRequirements(),
            request.getSalaryMin(), request.getSalaryMax(), request.getLocation(), request.getRemote()
        );
        return ResponseEntity.ok(JobSummaryDTO.from(job));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id, @AuthenticationPrincipal User user) {
        jobService.deleteJob(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/applications")
    @PreAuthorize("hasRole('EMPLOYER')")
    public Page<ApplicationDTO> getApplications(@AuthenticationPrincipal User user, Pageable pageable) {
        Page<Application> applications = jobService.getJobApplications(user, pageable);
        return new PageImpl<>(
            applications.getContent().stream()
                .map(ApplicationDTO::from)
                .collect(Collectors.toList()),
            pageable,
            applications.getTotalElements()
        );
    }

    @PutMapping("/applications/{id}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApplicationDTO> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody UpdateApplicationStatusRequest body,
            @AuthenticationPrincipal User user) {
        Application.Status targetStatus = mapFrontendStatus(body.getStatus());
        Application application = jobService.updateApplicationStatus(id, targetStatus, user);
        return ResponseEntity.ok(ApplicationDTO.from(application));
    }

    private Application.Status mapFrontendStatus(String status) {
        if (status == null) throw new IllegalArgumentException("status is required");
        return switch (status.toUpperCase()) {
            case "APPROVED", "ACCEPTED" -> Application.Status.ACCEPTED;
            case "REJECTED" -> Application.Status.REJECTED;
            case "INTERVIEW", "REVIEWED" -> Application.Status.REVIEWED;
            case "PENDING" -> Application.Status.PENDING;
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        };
    }

    @Data
    public static class CreateJobRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String description;
        private String requirements;
        private BigDecimal salaryMin;
        private BigDecimal salaryMax;
        private String location;
        private Boolean remote = false;
    }

    @Data
    public static class UpdateJobRequest {
        private String title;
        private String description;
        private String requirements;
        private BigDecimal salaryMin;
        private BigDecimal salaryMax;
        private String location;
        private Boolean remote;
    }

    @Data
    public static class UpdateApplicationStatusRequest {
        private String status;
    }
}
