package com.portalvagas.web;

import com.portalvagas.domain.Job;
import com.portalvagas.domain.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/jobs")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicJobController {

    private final JobRepository jobRepository;

    @GetMapping
    public Map<String, Object> getPublicJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean remote,
            Pageable pageable) {
        
        String t = title != null ? title : "";
        String loc = location != null ? location : "";
        Page<Job> jobs;
        if (remote != null) {
            jobs = jobRepository.findByStatusAndTitleContainingIgnoreCaseAndLocationContainingIgnoreCaseAndRemote(
                Job.Status.APPROVED, t, loc, remote, pageable);
        } else {
            jobs = jobRepository.findByStatusAndTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
                Job.Status.APPROVED, t, loc, pageable);
        }
        
        List<Map<String, Object>> content = jobs.getContent().stream()
            .map(this::mapToSimpleDto)
            .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", content);
        response.put("totalElements", jobs.getTotalElements());
        response.put("totalPages", jobs.getTotalPages());
        response.put("size", jobs.getSize());
        response.put("number", jobs.getNumber());
        response.put("first", jobs.isFirst());
        response.put("last", jobs.isLast());
        
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .filter(job -> job.getStatus() == Job.Status.APPROVED)
                .map(this::mapToSimpleDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private Map<String, Object> mapToSimpleDto(Job job) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", job.getId());
        dto.put("title", job.getTitle());
        dto.put("description", job.getDescription());
        dto.put("requirements", job.getRequirements());
        dto.put("location", job.getLocation());
        dto.put("remote", job.getRemote() != null ? job.getRemote() : false);
        dto.put("salaryMin", job.getSalaryMin() != null ? job.getSalaryMin().doubleValue() : null);
        dto.put("salaryMax", job.getSalaryMax() != null ? job.getSalaryMax().doubleValue() : null);
        dto.put("type", "FULL_TIME");
        dto.put("status", job.getStatus().toString());
        dto.put("applicationsCount", 0);
        
        // Convert LocalDateTime to String
        dto.put("createdAt", job.getCreatedAt() != null ? job.getCreatedAt().toString() : null);
        dto.put("updatedAt", job.getUpdatedAt() != null ? job.getUpdatedAt().toString() : null);
        
        // Company info
        if (job.getCompany() != null) {
            Map<String, Object> company = new HashMap<>();
            company.put("id", job.getCompany().getId());
            company.put("name", job.getCompany().getName());
            company.put("description", job.getCompany().getDescription());
            company.put("website", job.getCompany().getWebsite());
            company.put("verified", job.getCompany().getVerified() != null ? job.getCompany().getVerified() : false);
            dto.put("company", company);
        }
        
        return dto;
    }
}
