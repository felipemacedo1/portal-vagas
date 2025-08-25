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

    @GetMapping("/stats/candidate")
    public Map<String, Object> getCandidateStats(Authentication authentication) {
        // Mock stats for now - will be implemented with real queries
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalApplications", 0);
        stats.put("pendingApplications", 0);
        stats.put("approvedApplications", 0);
        stats.put("rejectedApplications", 0);
        stats.put("profileCompleteness", 85);
        
        return stats;
    }

    @GetMapping("/stats/employer")
    public Map<String, Object> getEmployerStats(Authentication authentication) {
        // Mock stats for now - will be implemented with real queries
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", 0);
        stats.put("activeJobs", 0);
        stats.put("totalApplications", 0);
        stats.put("pendingApplications", 0);
        
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
}