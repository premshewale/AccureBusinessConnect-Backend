package com.accuresoftech.abc.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accuresoftech.abc.entity.auth.Expense;
import com.accuresoftech.abc.enums.ExpenseStatus;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByDeletedFalse();
	List<Expense> findByDepartmentIdAndDeletedFalse(Long departmentId);
	List<Expense> findByOwnerIdAndDeletedFalse(Long ownerId);
	List<Expense> findByStatusAndDeletedFalse(ExpenseStatus status);
	List<Expense> findByStatusAndDepartment_IdAndDeletedFalse(ExpenseStatus status, Long departmentId);
	List<Expense> findByRelatedCustomerIdAndDeletedFalse(Long customerId);
	List<Expense> findByStatusAndOwnerIdAndDeletedFalse(ExpenseStatus status, Long ownerId);

	// List<Expense> findByDepartmentIdAndDateBetween(Long departmentId, LocalDate start, LocalDate end);
	 List<Expense> findByDepartmentIdAndDateBetweenAndDeletedFalse(Long departmentId, LocalDate start, LocalDate end);
	
}