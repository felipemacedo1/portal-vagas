package com.portalvagas.web;

import com.portalvagas.domain.Job;
import com.portalvagas.domain.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs/public")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicJobController {

    private final JobRepository jobRepository;

    @GetMapping
    public Page<Job> getPublicJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean remote,
            Pageable pageable) {
        return jobRepository.findByStatus(Job.Status.APPROVED, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .filter(job -> job.getStatus() == Job.Status.APPROVED)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}