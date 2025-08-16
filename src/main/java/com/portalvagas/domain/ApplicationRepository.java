package com.portalvagas.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);
    
    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);
    
    @Query("SELECT a FROM Application a JOIN a.job j WHERE j.company.id = :companyId")
    Page<Application> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);
}