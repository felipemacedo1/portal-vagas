package com.portalvagas.config;

import com.portalvagas.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            loadInitialData();
        }
    }

    private void loadInitialData() {
        // Criar usuário admin
        User admin = new User();
        admin.setEmail("admin@portalvagas.com");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        // Criar usuário employer
        User employer = new User();
        employer.setEmail("employer@test.com");
        employer.setPassword(passwordEncoder.encode("password"));
        employer.setRole(User.Role.EMPLOYER);
        userRepository.save(employer);

        // Criar empresa
        Company company = new Company();
        company.setName("Tech Solutions");
        company.setDescription("Empresa de tecnologia focada em soluções inovadoras");
        company.setWebsite("https://techsolutions.com");
        company.setVerified(true);
        company.setUser(employer);
        companyRepository.save(company);

        // Criar vaga aprovada
        Job job = new Job();
        job.setTitle("Desenvolvedor Java");
        job.setDescription("Desenvolvedor Java com experiência em Spring Boot");
        job.setRequirements("Java, Spring Boot, PostgreSQL");
        job.setSalaryMin(new BigDecimal("5000.00"));
        job.setSalaryMax(new BigDecimal("8000.00"));
        job.setLocation("São Paulo, SP");
        job.setRemote(true);
        job.setStatus(Job.Status.APPROVED);
        job.setCompany(company);
        jobRepository.save(job);

        // Criar usuário candidate
        User candidate = new User();
        candidate.setEmail("candidate@test.com");
        candidate.setPassword(passwordEncoder.encode("password"));
        candidate.setRole(User.Role.CANDIDATE);
        userRepository.save(candidate);

        System.out.println("Dados iniciais carregados com sucesso!");
    }
}