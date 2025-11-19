package com.accuresoftech.abc.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.accuresoftech.abc.enums.ExpenseCategory;
import com.accuresoftech.abc.enums.ExpenseStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseResponse {

	private Long id;
	private ExpenseCategory category;
	private BigDecimal amount;
	private LocalDate date;
	private String description;
	private Long relatedCustomerId;
	private String relatedCustomerName;
	private ExpenseStatus status;
	private Long departmentId;
	private String departmentName;
	private Long ownerId;
	private String ownerName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}