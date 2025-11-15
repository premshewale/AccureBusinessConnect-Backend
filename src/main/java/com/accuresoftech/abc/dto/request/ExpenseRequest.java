package com.accuresoftech.abc.dto.request;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.accuresoftech.abc.enums.ExpenseCategory;
import com.accuresoftech.abc.enums.ExpenseStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ExpenseRequest {

    @NotNull(message = "Category is required")
    private ExpenseCategory category;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    private LocalDate date;


    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;


    // Optional
    private Long relatedCustomerId;

    // Optional on create — for staff we will default to user's dept and owner
    private Long departmentId;

    private Long ownerId;

    // Optional: only admins/managers may set status on update
    private ExpenseStatus status;
}