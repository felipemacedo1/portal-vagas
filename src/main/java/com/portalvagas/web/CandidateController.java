package com.portalvagas.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    @GetMapping("/applications")
    public Map<String, Object> getCandidateApplications(
            Authentication authentication,
            Pageable pageable) {
        
        // Mock data for now - will be replaced with real implementation
        Map<String, Object> response = new HashMap<>();
        response.put("content", List.of());
        response.put("totalElements", 0);
        response.put("totalPages", 0);
        response.put("size", pageable.getPageSize());
        response.put("number", pageable.getPageNumber());
        response.put("first", true);
        response.put("last", true);
        
        return response;
    }

    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @RequestBody Map<String, Object> profileData,
            Authentication authentication) {
        
        // Mock response - will be replaced with real implementation
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profile updated successfully");
        response.put("data", profileData);
        
        return ResponseEntity.ok(response);
    }
}