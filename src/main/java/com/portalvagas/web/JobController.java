package com.portalvagas.web;

import com.portalvagas.domain.Application;
import com.portalvagas.domain.Job;
import com.portalvagas.domain.User;
import com.portalvagas.service.JobService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Job> createJob(
            @Valid @RequestBody CreateJobRequest request,
            @AuthenticationPrincipal User user) {
        
        Job job = jobService.createJob(
                user, request.getTitle(), request.getDescription(), request.getRequirements(),
                request.getSalaryMin(), request.getSalaryMax(), request.getLocation(), request.getRemote());
        
        return ResponseEntity.ok(job);
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Job> submitJob(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Job job = jobService.submitJob(id, user);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/applications")
    @PreAuthorize("hasRole('EMPLOYER')")
    public Page<Application> getApplications(@AuthenticationPrincipal User user, Pageable pageable) {
        return jobService.getJobApplications(user, pageable);
    }

    @PutMapping("/applications/{id}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam Application.Status status,
            @AuthenticationPrincipal User user) {
        
        Application application = jobService.updateApplicationStatus(id, status, user);
        return ResponseEntity.ok(application);
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
}