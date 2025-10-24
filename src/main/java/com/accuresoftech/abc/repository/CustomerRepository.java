package com.accuresoftech.abc.repository;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	 List<Customer> findByOwnerId(User owner);
	    List<Customer> findByDepartment(Department department);
	    List<Customer> findByOwnerId(Long ownerId);
}