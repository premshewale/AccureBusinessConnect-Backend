package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByDepartmentId(Long departmentId);
    long count(); // total 

    // Count by department
    long countByDepartmentId(Long departmentId);
}

