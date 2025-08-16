package com.portalvagas.web;

import com.portalvagas.domain.Company;
import com.portalvagas.domain.User;
import com.portalvagas.service.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Company> createCompany(
            @Valid @RequestBody CreateCompanyRequest request,
            @AuthenticationPrincipal User user) {
        
        Company existingCompany = companyService.getCompanyByUser(user);
        if (existingCompany != null) {
            return ResponseEntity.badRequest().build();
        }

        Company company = companyService.createCompany(
                user, request.getName(), request.getDescription(), request.getWebsite());
        return ResponseEntity.ok(company);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Company> getMyCompany(@AuthenticationPrincipal User user) {
        Company company = companyService.getCompanyByUser(user);
        return company != null ? ResponseEntity.ok(company) : ResponseEntity.notFound().build();
    }

    @Data
    public static class CreateCompanyRequest {
        @NotBlank
        private String name;
        private String description;
        private String website;
    }
}