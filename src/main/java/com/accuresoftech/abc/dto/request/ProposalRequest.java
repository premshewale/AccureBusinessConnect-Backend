package com.accuresoftech.abc.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProposalRequest {

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be positive")
    private BigDecimal budget;

    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be in the future")
    private LocalDate deadline;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;




}