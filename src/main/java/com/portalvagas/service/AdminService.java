package com.portalvagas.service;

import com.portalvagas.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    public Page<Job> getPendingJobs(Pageable pageable) {
        return jobRepository.findByStatus(Job.Status.PENDING, pageable);
    }

    public Job moderateJob(Long jobId, Job.Status newStatus) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (job.getStatus() != Job.Status.PENDING) {
            throw new RuntimeException("Job is not pending moderation");
        }

        if (newStatus != Job.Status.APPROVED && newStatus != Job.Status.REJECTED) {
            throw new RuntimeException("Invalid status for moderation");
        }

        job.setStatus(newStatus);
        return jobRepository.save(job);
    }

    public Company verifyCompany(Long companyId, boolean verified) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setVerified(verified);
        return companyRepository.save(company);
    }

    public Page<Company> getUnverifiedCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }
}