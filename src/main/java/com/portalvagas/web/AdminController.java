package com.portalvagas.web;

import com.portalvagas.domain.Company;
import com.portalvagas.domain.Job;
import com.portalvagas.dto.CompanyDTO;
import com.portalvagas.dto.JobSummaryDTO;
import com.portalvagas.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/jobs/pending")
    public Page<JobSummaryDTO> getPendingJobs(Pageable pageable) {
        Page<Job> jobs = adminService.getPendingJobs(pageable);
        return new PageImpl<>(
            jobs.getContent().stream()
                .map(JobSummaryDTO::from)
                .collect(Collectors.toList()),
            pageable,
            jobs.getTotalElements()
        );
    }

    @PostMapping("/jobs/{id}/approve")
    public ResponseEntity<JobSummaryDTO> approveJob(@PathVariable Long id) {
        Job job = adminService.moderateJob(id, Job.Status.APPROVED);
        return ResponseEntity.ok(JobSummaryDTO.from(job));
    }

    @PostMapping("/jobs/{id}/reject")
    public ResponseEntity<JobSummaryDTO> rejectJob(@PathVariable Long id) {
        Job job = adminService.moderateJob(id, Job.Status.REJECTED);
        return ResponseEntity.ok(JobSummaryDTO.from(job));
    }

    @GetMapping("/companies")
    public Page<CompanyDTO> getCompanies(Pageable pageable) {
        Page<Company> companies = adminService.getUnverifiedCompanies(pageable);
        return new PageImpl<>(
            companies.getContent().stream()
                .map(CompanyDTO::from)
                .collect(Collectors.toList()),
            pageable,
            companies.getTotalElements()
        );
    }

    @PostMapping("/companies/{id}/verify")
    public ResponseEntity<CompanyDTO> verifyCompany(@PathVariable Long id) {
        Company company = adminService.verifyCompany(id, true);
        return ResponseEntity.ok(CompanyDTO.from(company));
    }

    @PostMapping("/companies/{id}/unverify")
    public ResponseEntity<CompanyDTO> unverifyCompany(@PathVariable Long id) {
        Company company = adminService.verifyCompany(id, false);
        return ResponseEntity.ok(CompanyDTO.from(company));
    }
}