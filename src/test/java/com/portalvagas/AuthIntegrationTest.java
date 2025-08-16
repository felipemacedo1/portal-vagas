package com.portalvagas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Test
    public void contextLoads() {
        // Test that Spring context loads successfully
        assertTrue(true);
    }
}