package com.portalvagas.web;

import com.portalvagas.domain.Company;
import com.portalvagas.domain.Job;
import com.portalvagas.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/jobs/pending")
    public Page<Job> getPendingJobs(Pageable pageable) {
        return adminService.getPendingJobs(pageable);
    }

    @PostMapping("/jobs/{id}/approve")
    public ResponseEntity<Job> approveJob(@PathVariable Long id) {
        Job job = adminService.moderateJob(id, Job.Status.APPROVED);
        return ResponseEntity.ok(job);
    }

    @PostMapping("/jobs/{id}/reject")
    public ResponseEntity<Job> rejectJob(@PathVariable Long id) {
        Job job = adminService.moderateJob(id, Job.Status.REJECTED);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/companies")
    public Page<Company> getCompanies(Pageable pageable) {
        return adminService.getUnverifiedCompanies(pageable);
    }

    @PostMapping("/companies/{id}/verify")
    public ResponseEntity<Company> verifyCompany(@PathVariable Long id) {
        Company company = adminService.verifyCompany(id, true);
        return ResponseEntity.ok(company);
    }

    @PostMapping("/companies/{id}/unverify")
    public ResponseEntity<Company> unverifyCompany(@PathVariable Long id) {
        Company company = adminService.verifyCompany(id, false);
        return ResponseEntity.ok(company);
    }
}