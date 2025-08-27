package com.portalvagas.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByStatus(Job.Status status, Pageable pageable);
    
    Page<Job> findByCompanyId(Long companyId, Pageable pageable);
    
    long countByStatus(Job.Status status);

    long countByCompanyId(Long companyId);

    long countByStatusAndCompanyId(Job.Status status, Long companyId);

    Page<Job> findByStatusAndTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
        Job.Status status, String title, String location, Pageable pageable);

    Page<Job> findByStatusAndTitleContainingIgnoreCaseAndLocationContainingIgnoreCaseAndRemote(
        Job.Status status, String title, String location, Boolean remote, Pageable pageable);
}
