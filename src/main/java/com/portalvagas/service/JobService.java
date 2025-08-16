package com.portalvagas.service;

import com.portalvagas.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;

    public Job createJob(User user, String title, String description, String requirements,
                        BigDecimal salaryMin, BigDecimal salaryMax, String location, Boolean remote) {
        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Job job = new Job();
        job.setCompany(company);
        job.setTitle(title);
        job.setDescription(description);
        job.setRequirements(requirements);
        job.setSalaryMin(salaryMin);
        job.setSalaryMax(salaryMax);
        job.setLocation(location);
        job.setRemote(remote);
        job.setStatus(Job.Status.DRAFT);

        return jobRepository.save(job);
    }

    public Job submitJob(Long jobId, User user) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCompany().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (job.getStatus() != Job.Status.DRAFT) {
            throw new RuntimeException("Job cannot be submitted");
        }

        job.setStatus(Job.Status.PENDING);
        return jobRepository.save(job);
    }

    public Page<Application> getJobApplications(User user, Pageable pageable) {
        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        
        return applicationRepository.findByCompanyId(company.getId(), pageable);
    }
}