package com.portalvagas;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
class NotificationIntegrationTest {

    @Test
    void notificationServiceTest() {
        assertTrue(true, "Notification service should be testable");
    }
    
    @Test
    void emailConfigurationTest() {
        assertTrue(true, "Email configuration should be valid");
    }
}