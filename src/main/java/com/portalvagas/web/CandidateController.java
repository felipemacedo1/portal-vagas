package com.portalvagas.web;

import com.portalvagas.domain.Application;
import com.portalvagas.domain.Candidate;
import com.portalvagas.domain.User;
import com.portalvagas.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PutMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Candidate> updateProfile(
            @RequestParam String fullName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) MultipartFile cv,
            @AuthenticationPrincipal User user) throws IOException {
        
        Candidate candidate = candidateService.updateProfile(user, fullName, phone, cv);
        return ResponseEntity.ok(candidate);
    }

    @PostMapping("/applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Application> applyToJob(
            @RequestParam Long jobId,
            @AuthenticationPrincipal User user) {
        
        Application application = candidateService.applyToJob(user, jobId);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public Page<Application> getMyApplications(@AuthenticationPrincipal User user, Pageable pageable) {
        return candidateService.getMyApplications(user, pageable);
    }
}