package com.portalvagas.web;

import com.portalvagas.domain.Job;
import com.portalvagas.domain.JobRepository;
import com.portalvagas.dto.JobDTO;
import com.portalvagas.dto.JobSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobs/public")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicJobController {

    private final JobRepository jobRepository;

    @GetMapping
    public Page<JobSummaryDTO> getPublicJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean remote,
            Pageable pageable) {
        
        Page<Job> jobs = jobRepository.findByStatus(Job.Status.APPROVED, pageable);
        
        return new PageImpl<>(
            jobs.getContent().stream()
                .map(JobSummaryDTO::from)
                .collect(Collectors.toList()),
            pageable,
            jobs.getTotalElements()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .filter(job -> job.getStatus() == Job.Status.APPROVED)
                .map(JobDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}