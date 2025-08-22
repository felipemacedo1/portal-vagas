package com.portalvagas.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByStatus(Job.Status status, Pageable pageable);
    
    Page<Job> findByCompanyId(Long companyId, Pageable pageable);
}