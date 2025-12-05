package com.accuresoftech.abc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accuresoftech.abc.entity.auth.LeadActivity;

@Repository
public interface LeadActivityRepository extends JpaRepository<LeadActivity, Long>
{
    List<LeadActivity> findByLeadIdOrderByCreatedAtDesc(Long leadId);
}