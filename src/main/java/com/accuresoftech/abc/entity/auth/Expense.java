package com.accuresoftech.abc.entity.auth;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.accuresoftech.abc.entity.BaseEntity;
import com.accuresoftech.abc.enums.ExpenseCategory;
import com.accuresoftech.abc.enums.ExpenseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ExpenseCategory category; // TRAVEL, MARKETING, SOFTWARE, SALARY, OTHER

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false)
	private LocalDate date;

	@Column(columnDefinition = "TEXT")
	private String description;

	// Optional relation to customer (null allowed)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "related_customer_id", nullable = true)
	private Customer relatedCustomer;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ExpenseStatus status = ExpenseStatus.PENDING; // default pending

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner;

	// The Owner means:
	// “Who created this record?” or “Who is responsible for this record?”

	// Soft delete flag
	@Column(nullable = false)
	private boolean deleted = false;

}