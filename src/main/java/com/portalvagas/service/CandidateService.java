package com.portalvagas.service;

import com.portalvagas.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final NotificationService notificationService;

    public Candidate updateProfile(User user, String fullName, String phone, MultipartFile cvFile) throws IOException {
        Candidate candidate = candidateRepository.findByUserId(user.getId())
                .orElse(new Candidate());

        if (candidate.getId() == null) {
            candidate.setUser(user);
        }

        candidate.setFullName(fullName);
        candidate.setPhone(phone);

        if (cvFile != null && !cvFile.isEmpty()) {
            candidate.setCvFilename(cvFile.getOriginalFilename());
            candidate.setCvContentType(cvFile.getContentType());
            candidate.setCvData(cvFile.getBytes());
        }

        return candidateRepository.save(candidate);
    }

    public Application applyToJob(User user, Long jobId) {
        Candidate candidate = candidateRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Candidate profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (job.getStatus() != Job.Status.APPROVED) {
            throw new RuntimeException("Job is not available for applications");
        }

        if (applicationRepository.existsByCandidateIdAndJobId(candidate.getId(), jobId)) {
            throw new RuntimeException("Already applied to this job");
        }

        Application application = new Application();
        application.setCandidate(candidate);
        application.setJob(job);
        application.setStatus(Application.Status.PENDING);

        Application savedApplication = applicationRepository.save(application);
        notificationService.notifyNewApplication(savedApplication);
        
        return savedApplication;
    }

    public Page<Application> getMyApplications(User user, Pageable pageable) {
        Candidate candidate = candidateRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Candidate profile not found"));
        
        return applicationRepository.findByCandidateId(candidate.getId(), pageable);
    }
}