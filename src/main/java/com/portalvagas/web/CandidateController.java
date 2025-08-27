package com.portalvagas.web;

import com.portalvagas.domain.Application;
import com.portalvagas.domain.User;
import com.portalvagas.dto.ApplicationDTO;
import com.portalvagas.dto.ApplicationSummaryDTO;
import com.portalvagas.service.CandidateService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("/applications")
    public Page<ApplicationDTO> getCandidateApplications(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        Page<Application> apps = candidateService.getMyApplications(user, pageable);
        return new PageImpl<>(
                apps.getContent().stream().map(ApplicationDTO::from).collect(Collectors.toList()),
                pageable,
                apps.getTotalElements()
        );
    }

    @PostMapping("/applications")
    public ResponseEntity<ApplicationDTO> applyToJob(
            @RequestParam Long jobId,
            @AuthenticationPrincipal User user,
            @RequestBody(required = false) ApplyRequest body) {
        // coverLetter é opcional e ainda não é persistido; ignoramos por ora
        Application application = candidateService.applyToJob(user, jobId);
        return ResponseEntity.ok(ApplicationDTO.from(application));
    }

    @Data
    public static class ApplyRequest {
        private String coverLetter;
    }
}
