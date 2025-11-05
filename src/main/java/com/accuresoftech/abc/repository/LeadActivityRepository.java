package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.LeadActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadActivityRepository extends JpaRepository<LeadActivity, Long> 
{
    List<LeadActivity> findByLeadIdOrderByCreatedAtDesc(Long leadId);
}