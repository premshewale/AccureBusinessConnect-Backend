package com.accuresoftech.abc.services;

import java.time.LocalDate;
import java.util.List;

import com.accuresoftech.abc.dto.request.ExpenseRequest;
import com.accuresoftech.abc.dto.response.ExpenseResponse;

public interface ExpenseService {

	ExpenseResponse createExpense(ExpenseRequest request);

	List<ExpenseResponse> getAllExpenses();

	ExpenseResponse getExpenseById(Long id);

	ExpenseResponse updateExpense(Long id, ExpenseRequest request);

	void deleteExpense(Long id);

	ExpenseResponse updateExpenseStatus(Long id, String status);

	List<ExpenseResponse> getByDepartment(Long departmentId);
	
  List<ExpenseResponse> getExpensesByDeptAndDateRange(Long departmentId, LocalDate start, LocalDate end);

  List<ExpenseResponse> getByStatus(String status);

}