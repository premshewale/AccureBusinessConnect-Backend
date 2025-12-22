package com.accuresoftech.abc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accuresoftech.abc.entity.auth.SupportTicket;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByDepartmentId(Long departmentId);
    List<SupportTicket> findByAssignedToId(Long userId);
}
