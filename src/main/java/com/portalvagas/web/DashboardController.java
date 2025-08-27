package com.portalvagas.web;

import com.portalvagas.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;

    @GetMapping("/stats/candidate")
    public Map<String, Object> getCandidateStats(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Candidate candidate = candidateRepository.findByUserId(user.getId())
                .orElse(null);
        Map<String, Object> stats = new HashMap<>();
        if (candidate == null) {
            stats.put("totalApplications", 0);
            stats.put("pendingApplications", 0);
            stats.put("approvedApplications", 0);
            stats.put("rejectedApplications", 0);
            stats.put("profileCompleteness", 0);
            return stats;
        }

        long total = applicationRepository.countByCandidateId(candidate.getId());
        long pending = applicationRepository.countByStatusAndCandidateId(Application.Status.PENDING, candidate.getId());
        long accepted = applicationRepository.countByStatusAndCandidateId(Application.Status.ACCEPTED, candidate.getId());
        long rejected = applicationRepository.countByStatusAndCandidateId(Application.Status.REJECTED, candidate.getId());

        stats.put("totalApplications", total);
        stats.put("pendingApplications", pending);
        stats.put("approvedApplications", accepted);
        stats.put("rejectedApplications", rejected);

        int completeness = 0;
        int fields = 0;
        if (candidate.getFullName() != null && !candidate.getFullName().isBlank()) { completeness += 25; fields++; }
        if (candidate.getPhone() != null && !candidate.getPhone().isBlank()) { completeness += 25; fields++; }
        if (candidate.getCvData() != null) { completeness += 25; fields++; }
        if (candidate.getUser() != null && candidate.getUser().getEmail() != null) { completeness += 25; fields++; }
        stats.put("profileCompleteness", completeness);

        return stats;
    }

    @GetMapping("/stats/employer")
    public Map<String, Object> getEmployerStats(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Company company = companyRepository.findByUserId(user.getId()).orElse(null);
        Map<String, Object> stats = new HashMap<>();
        if (company == null) {
            stats.put("totalJobs", 0);
            stats.put("activeJobs", 0);
            stats.put("totalApplications", 0);
            stats.put("pendingApplications", 0);
            return stats;
        }

        long totalJobs = jobRepository.countByCompanyId(company.getId());
        long activeJobs = jobRepository.countByStatusAndCompanyId(Job.Status.APPROVED, company.getId());
        long totalApplications = applicationRepository.countByJobCompanyId(company.getId());
        long pendingApplications = applicationRepository.countByStatusAndJobCompanyId(Application.Status.PENDING, company.getId());

        stats.put("totalJobs", totalJobs);
        stats.put("activeJobs", activeJobs);
        stats.put("totalApplications", totalApplications);
        stats.put("pendingApplications", pendingApplications);
        return stats;
    }

    @GetMapping("/stats/admin")
    public Map<String, Object> getAdminStats(Authentication authentication) {
        long pendingJobs = jobRepository.countByStatus(Job.Status.PENDING);
        long totalUsers = userRepository.count();
        long totalCompanies = companyRepository.count();
        long unverifiedCompanies = companyRepository.countByVerified(false);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("pendingJobs", pendingJobs);
        stats.put("unverifiedCompanies", unverifiedCompanies);
        stats.put("totalUsers", totalUsers);
        stats.put("totalCompanies", totalCompanies);
        stats.put("todayApprovals", 0); // Will be implemented with date queries
        
        return stats;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return Map.of();
        }
        User user = (User) authentication.getPrincipal();
        return switch (user.getRole()) {
            case ADMIN -> getAdminStats(authentication);
            case EMPLOYER -> getEmployerStats(authentication);
            case CANDIDATE -> getCandidateStats(authentication);
        };
    }
}
