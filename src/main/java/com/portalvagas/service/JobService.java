package com.portalvagas.service;

import com.portalvagas.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;
    private final NotificationService notificationService;

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

    public Page<Job> getEmployerJobs(User user, Pageable pageable) {
        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return jobRepository.findByCompanyId(company.getId(), pageable);
    }

    public Job updateJob(Long jobId, User user, String title, String description, String requirements,
                         BigDecimal salaryMin, BigDecimal salaryMax, String location, Boolean remote) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCompany().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (job.getStatus() != Job.Status.DRAFT) {
            throw new RuntimeException("Only DRAFT jobs can be updated");
        }

        if (title != null) job.setTitle(title);
        if (description != null) job.setDescription(description);
        if (requirements != null) job.setRequirements(requirements);
        if (salaryMin != null) job.setSalaryMin(salaryMin);
        if (salaryMax != null) job.setSalaryMax(salaryMax);
        if (location != null) job.setLocation(location);
        if (remote != null) job.setRemote(remote);

        return jobRepository.save(job);
    }

    public void deleteJob(Long jobId, User user) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCompany().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (job.getStatus() == Job.Status.APPROVED || job.getStatus() == Job.Status.PENDING) {
            throw new RuntimeException("Cannot delete approved or pending jobs");
        }

        jobRepository.delete(job);
    }

    public Page<Application> getJobApplications(User user, Pageable pageable) {
        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        
        return applicationRepository.findByCompanyId(company.getId(), pageable);
    }

    public Application updateApplicationStatus(Long applicationId, Application.Status newStatus, User user) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (!application.getJob().getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        application.setStatus(newStatus);
        Application savedApplication = applicationRepository.save(application);
        
        notificationService.notifyApplicationStatusChange(savedApplication);
        
        return savedApplication;
    }

    public Page<JobSummaryDTO> getEmployerJobSummaries(User user, Pageable pageable) {
        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Page<Job> jobs = jobRepository.findByCompanyId(company.getId(), pageable);
        return new PageImpl<>(
            jobs.getContent().stream().map(job -> {
                JobSummaryDTO dto = JobSummaryDTO.from(job);
                int count = (int) applicationRepository.countByJobId(job.getId());
                dto.setApplicationsCount(count);
                return dto;
            }).toList(),
            pageable,
            jobs.getTotalElements()
        );
    }
}
