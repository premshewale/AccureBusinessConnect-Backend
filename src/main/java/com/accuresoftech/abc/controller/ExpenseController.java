package com.accuresoftech.abc.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accuresoftech.abc.dto.request.ExpenseRequest;
import com.accuresoftech.abc.dto.response.ExpenseResponse;
import com.accuresoftech.abc.services.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.createExpense(request); // current user fetched inside service
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        ExpenseResponse expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse expense = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ExpenseResponse> updateExpenseStatus(@PathVariable Long id, @RequestParam String status) {
        ExpenseResponse expense = expenseService.updateExpenseStatus(id, status);
        return ResponseEntity.ok(expense);
    }

    // Expense report filter- department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ExpenseResponse>> getByDepartment(@PathVariable Long departmentId) {
        List<ExpenseResponse> expenses = expenseService.getByDepartment(departmentId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ExpenseResponse>> getByStatus(@PathVariable String status) {
        List<ExpenseResponse> expenses = expenseService.getByStatus(status);
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/department/{departmentId}/range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDeptAndDateRange(
            @PathVariable Long departmentId,
            @RequestParam("start") String startDate,
            @RequestParam("end") String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<ExpenseResponse> expenses = expenseService.getExpensesByDeptAndDateRange(departmentId, start, end);
        return ResponseEntity.ok(expenses);
    }
}