package com.accuresoftech.abc.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.accuresoftech.abc.entity.auth.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long>
{

    Page<Customer> findByAssignedUserId(Long userId, Pageable pageable);

    Page<Customer> findByDepartment_Id(Long deptId, Pageable pageable);


    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);


    @Query("SELECT c FROM Customer c WHERE c.deleted = false AND (" +
    	       "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
    	       "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
    	       "LOWER(c.phone) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Customer> searchGlobal(@Param("search") String search, Pageable pageable);
    
   
    long count(); // total 

    // Count by department
    long countByDepartmentId(Long departmentId);

    // Count by assigned user
    long countByAssignedUser_Id(Long userId);
    
    @Query("""
            SELECT c FROM Customer c
            WHERE (:from IS NULL OR c.createdAt >= :from)
            AND (:to IS NULL OR c.createdAt <= :to)
        """)
        List<Customer> findCustomersForGrowthReport(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
        );
}
