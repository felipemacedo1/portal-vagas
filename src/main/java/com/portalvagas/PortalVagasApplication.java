package com.portalvagas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PortalVagasApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortalVagasApplication.class, args);
    }
}
