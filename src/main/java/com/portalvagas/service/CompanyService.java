package com.portalvagas.service;

import com.portalvagas.domain.Company;
import com.portalvagas.domain.CompanyRepository;
import com.portalvagas.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company createCompany(User user, String name, String description, String website) {
        Company company = new Company();
        company.setUser(user);
        company.setName(name);
        company.setDescription(description);
        company.setWebsite(website);
        return companyRepository.save(company);
    }

    public Company getCompanyByUser(User user) {
        return companyRepository.findByUserId(user.getId()).orElse(null);
    }
}