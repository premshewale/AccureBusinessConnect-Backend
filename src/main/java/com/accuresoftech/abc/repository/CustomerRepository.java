package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByDepartmentId(Long departmentId);
    List<Customer> findByAssignedUserId(Long userId);
    List<Customer> findAllByAssignedUserIdAndDepartmentId(Long assignedUserId, Long departmentId);
    List<Customer> findAllByDepartmentId(Long departmentId);
    List<Customer> findAllByAssignedUserId(Long userId);
    //List<Customer> findAllByAssignedUserIdAndDepartmentId(Long assignedUserId, Long departmentId);
}

