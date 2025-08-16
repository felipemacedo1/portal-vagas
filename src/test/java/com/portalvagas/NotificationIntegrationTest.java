package com.portalvagas;

import com.portalvagas.domain.*;
import com.portalvagas.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NotificationIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void shouldSendEmailOnNewApplication() throws InterruptedException {
        // Given
        User user = createUser("employer@test.com", User.Role.EMPLOYER);
        User candidate = createUser("candidate@test.com", User.Role.CANDIDATE);
        Company company = createCompany("Test Company", user);
        Job job = createJob("Test Job", company);
        Candidate candidateProfile = createCandidate("John Doe", candidate);
        Application application = createApplication(candidateProfile, job);

        // When
        notificationService.notifyNewApplication(application);

        // Then
        Thread.sleep(1000); // Wait for async processing
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    private User createUser(String email, User.Role role) {
        User user = new User();
        user.setEmail(email);
        user.setRole(role);
        return user;
    }

    private Company createCompany(String name, User user) {
        Company company = new Company();
        company.setName(name);
        company.setUser(user);
        return company;
    }

    private Job createJob(String title, Company company) {
        Job job = new Job();
        job.setTitle(title);
        job.setCompany(company);
        job.setDescription("Test description");
        return job;
    }

    private Candidate createCandidate(String fullName, User user) {
        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setUser(user);
        return candidate;
    }

    private Application createApplication(Candidate candidate, Job job) {
        Application application = new Application();
        application.setCandidate(candidate);
        application.setJob(job);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus(Application.Status.PENDING);
        return application;
    }
}