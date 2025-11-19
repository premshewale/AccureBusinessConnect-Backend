package com.accuresoftech.abc.servicesimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accuresoftech.abc.dto.request.ExpenseRequest;
import com.accuresoftech.abc.dto.response.ExpenseResponse;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.Expense;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.ExpenseStatus;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.ExpenseRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.ExpenseService;
import com.accuresoftech.abc.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final CustomerRepository customerRepository;
	private final DepartmentRepository departmentRepository;
	private final UserRepository userRepository;
	private final AuthUtils authUtils;

	private User getCurrentUser() {
		return authUtils.getCurrentUser();
	}

	// Helper: check if user can access given expense
	private boolean canAccessExpense(Expense expense, User user) {
		if (user.getRole().getKey() == RoleKey.ADMIN) {
			return true;
		}
		if (user.getRole().getKey() == RoleKey.SUB_ADMIN) {
			return expense.getDepartment() != null && user.getDepartment() != null
					&& expense.getDepartment().getId().equals(user.getDepartment().getId());
		}
		return expense.getOwner() != null && expense.getOwner().getId().equals(user.getId());
	}

	// Create Expense
	@Override
	@Transactional // write operation
	public ExpenseResponse createExpense(ExpenseRequest request) {
		User currentUser = getCurrentUser();
		if (currentUser == null) {
			throw new AccessDeniedException("Unauthorized");
		}

		// Ensure staff cannot manipulate department/owner
		if (currentUser.getRole().getKey() == RoleKey.STAFF) {
			request.setDepartmentId(currentUser.getDepartment().getId());
			request.setOwnerId(currentUser.getId());
		}

		Department department;
		if (request.getDepartmentId() != null) {
			department = departmentRepository.findById(request.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
		} else {
			department = currentUser.getDepartment();
		}

		Customer relatedCustomer = null;
		if (request.getRelatedCustomerId() != null) {
			relatedCustomer = customerRepository.findById(request.getRelatedCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		}

		Expense expense = Expense.builder().category(request.getCategory()).amount(request.getAmount())
				.date(request.getDate()).description(request.getDescription()).relatedCustomer(relatedCustomer)
				.department(department).owner(currentUser).status(ExpenseStatus.PENDING).build();

		expenseRepository.save(expense);
		return toResponse(expense);
	}

	// Get All Expenses (role-based)
	@Override
	@Transactional(readOnly = true)
	public List<ExpenseResponse> getAllExpenses() {
		User currentUser = getCurrentUser();
		List<Expense> expenses;

		if (currentUser.getRole().getKey() == RoleKey.ADMIN) {
			expenses = expenseRepository.findByDeletedFalse();
		} else if (currentUser.getRole().getKey() == RoleKey.SUB_ADMIN) {
			expenses = expenseRepository.findByDepartmentIdAndDeletedFalse(currentUser.getDepartment().getId());
		} else {
			expenses = expenseRepository.findByOwnerIdAndDeletedFalse(currentUser.getId());
		}

		return expenses.stream().map(this::toResponse).toList();
	}

	// Get Expense by ID
	@Override
	@Transactional(readOnly = true)
	public ExpenseResponse getExpenseById(Long id) {
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

		if (expense.isDeleted()) {
			throw new RuntimeException("Expense already deleted");
		}

		User currentUser = getCurrentUser();
		if (!canAccessExpense(expense, currentUser)) {
			throw new AccessDeniedException("Access denied");
		}

		return toResponse(expense);
	}

	// 🔹 Update Expense
	@Override
	@Transactional
	public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
		if (expense.isDeleted()) {
			throw new RuntimeException("Expense already deleted");
		}

		User currentUser = getCurrentUser();
		if (!canAccessExpense(expense, currentUser)) {
			throw new AccessDeniedException("You cannot update this expense");
		}

		if (request.getCategory() != null) {
			expense.setCategory(request.getCategory());
		}
		if (request.getAmount() != null) {
			expense.setAmount(request.getAmount());
		}
		if (request.getDate() != null) {
			expense.setDate(request.getDate());
		}
		if (request.getDescription() != null) {
			expense.setDescription(request.getDescription());
		}

		if (request.getRelatedCustomerId() != null) {
			Customer relatedCustomer = customerRepository.findById(request.getRelatedCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
			expense.setRelatedCustomer(relatedCustomer);
		}

		// Only Admin/SubAdmin can change department, owner, or status
		if (currentUser.getRole().getKey() != RoleKey.STAFF) {
			if (request.getDepartmentId() != null) {
				Department department = departmentRepository.findById(request.getDepartmentId())
						.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
				expense.setDepartment(department);
			}
			if (request.getOwnerId() != null) {
				User owner = userRepository.findById(request.getOwnerId())
						.orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
				expense.setOwner(owner);
			}
			if (request.getStatus() != null) {
				expense.setStatus(request.getStatus());
			}
		}

		return toResponse(expenseRepository.save(expense));
	}

	// Soft Delete
	@Override
	@Transactional
	public void deleteExpense(Long id) {
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
		User currentUser = getCurrentUser();
		if (!canAccessExpense(expense, currentUser)) {
			throw new AccessDeniedException("You can't delete this expense");
		}

		expense.setDeleted(true);
		expense.setUpdatedAt(LocalDateTime.now()); // optional timestamp
		expenseRepository.save(expense);
	}

	// Update Status (Admin/Sub-Admin only)
	@Override
	@Transactional
	public ExpenseResponse updateExpenseStatus(Long id, String status) {
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

		User currentUser = getCurrentUser();
		if (currentUser.getRole().getKey() != RoleKey.ADMIN && currentUser.getRole().getKey() != RoleKey.SUB_ADMIN) {
			throw new AccessDeniedException("Only Admin or SubAdmin can update expense status");
		}

		ExpenseStatus newStatus;
		try {
			newStatus = ExpenseStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException("Invalid status: " + status);
		}

		if (expense.getStatus() == ExpenseStatus.PENDING
				&& (newStatus == ExpenseStatus.APPROVED || newStatus == ExpenseStatus.REJECTED)) {
			expense.setStatus(newStatus);
		} else {
			throw new RuntimeException("Invalid status transition");
		}

		return toResponse(expenseRepository.save(expense));
	}

	// Get by Department
	@Override
	@Transactional(readOnly = true)
	public List<ExpenseResponse> getByDepartment(Long departmentId) {
		User currentUser = getCurrentUser();

		if (currentUser.getRole().getKey() == RoleKey.STAFF
				&& !currentUser.getDepartment().getId().equals(departmentId)) {
			throw new AccessDeniedException("Access denied for other departments");
		}

		return expenseRepository.findByDepartmentIdAndDeletedFalse(departmentId).stream().map(this::toResponse)
				.toList();
	}

	@Override
	public List<ExpenseResponse> getExpensesByDeptAndDateRange(Long departmentId, LocalDate start, LocalDate end) {
	    List<Expense> expenses = expenseRepository
	        .findByDepartmentIdAndDateBetweenAndDeletedFalse(departmentId, start, end); 
	    return expenses.stream()
	                   .map(this::toResponse) 
	                   .toList();
	}
	
	// Get by Status
	@Override
	@Transactional(readOnly = true)
	public List<ExpenseResponse> getByStatus(String status) {
		ExpenseStatus expenseStatus = ExpenseStatus.valueOf(status.toUpperCase());
		User currentUser = getCurrentUser();

		List<Expense> expenses;
		if (currentUser.getRole().getKey() == RoleKey.ADMIN) {
			expenses = expenseRepository.findByStatusAndDeletedFalse(expenseStatus);
		} else if (currentUser.getRole().getKey() == RoleKey.SUB_ADMIN) {
			expenses = expenseRepository.findByStatusAndDepartment_IdAndDeletedFalse(expenseStatus,
					currentUser.getDepartment().getId());
		} else {
			// Staff: only own expenses
			expenses = expenseRepository.findByStatusAndOwnerIdAndDeletedFalse(expenseStatus, currentUser.getId());
		}

		return expenses.stream().map(this::toResponse).toList();
	}

	// Mapper to Response DTO
	private ExpenseResponse toResponse(Expense e) {
		return ExpenseResponse.builder().id(e.getId()).category(e.getCategory()).amount(e.getAmount()).date(e.getDate())
				.description(e.getDescription()).status(e.getStatus())
				.relatedCustomerId(e.getRelatedCustomer() != null ? e.getRelatedCustomer().getId() : null)
				.relatedCustomerName(e.getRelatedCustomer() != null ? e.getRelatedCustomer().getName() : null)
				.departmentId(e.getDepartment() != null ? e.getDepartment().getId() : null)
				.departmentName(e.getDepartment() != null ? e.getDepartment().getName() : null)
				.ownerId(e.getOwner() != null ? e.getOwner().getId() : null)
				.ownerName(e.getOwner() != null ? e.getOwner().getName() : null).createdAt(e.getCreatedAt())
				.updatedAt(e.getUpdatedAt()).build();
	}
}