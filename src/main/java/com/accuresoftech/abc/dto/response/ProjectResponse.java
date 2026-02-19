package com.accuresoftech.abc.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.accuresoftech.abc.enums.BillingType;
import com.accuresoftech.abc.enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {

    private Long id;
    private String projectName;

    private String customerName;

    private ProjectStatus status;
    private BillingType billingType;

   
    private BigDecimal ratePerHour;
    private Integer estimatedHours;

    private LocalDate startDate;
    private LocalDate deadline;

    private Set<String> members;
    private Set<String> tags;

    private String description;
    private LocalDateTime createdAt;
    
    
}