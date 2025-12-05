package com.accuresoftech.abc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accuresoftech.abc.entity.auth.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByCustomerId(Long customerId);

    @Query("SELECT i FROM Invoice i WHERE i.department.id = :departmentId")
    List<Invoice> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT i FROM Invoice i WHERE i.status = :status")
    List<Invoice> findByStatus(@Param("status") String status);
    
    
    
    long count(); // total invoices

    long countByDepartment_Id(Long departmentId);

    long countByCreatedBy_Id(Long userId);
}
