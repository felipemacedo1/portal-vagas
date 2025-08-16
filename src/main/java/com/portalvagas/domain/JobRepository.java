package com.portalvagas.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByStatus(Job.Status status, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = 'APPROVED' " +
           "AND (:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:remote IS NULL OR j.remote = :remote)")
    Page<Job> findPublicJobs(@Param("title") String title, 
                           @Param("location") String location, 
                           @Param("remote") Boolean remote, 
                           Pageable pageable);
    
    Page<Job> findByCompanyId(Long companyId, Pageable pageable);
}