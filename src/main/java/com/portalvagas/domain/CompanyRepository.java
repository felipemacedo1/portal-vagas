package com.portalvagas.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    long countByVerified(Boolean verified);
    
    Optional<Company> findByUserId(Long userId);
}