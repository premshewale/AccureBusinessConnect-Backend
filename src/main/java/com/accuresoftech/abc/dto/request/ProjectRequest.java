package com.accuresoftech.abc.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.accuresoftech.abc.enums.BillingType;

import lombok.Data;

@Data
public class ProjectRequest {

    private String projectName;
    private Long customerId;

    private BillingType billingType;
   
    private BigDecimal ratePerHour;
    private Integer estimatedHours;

    private LocalDate startDate;
    private LocalDate deadline;

    private Set<Long> memberIds;
    private Set<Long> tagIds;

    private String description;
}