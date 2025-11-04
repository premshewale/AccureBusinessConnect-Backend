package com.accuresoftech.abc.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalResponse 
{
	private Long id;
    private BigDecimal budget;
    
  
    private String description;
  
    private String status;
    private LocalDate deadline;
    private String customerName;
    private String departmentName;
    private String ownerName;
    private LocalDateTime createdAt;
    
    
    
}