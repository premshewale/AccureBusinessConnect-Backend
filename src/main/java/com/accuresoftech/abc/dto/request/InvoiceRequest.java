package com.accuresoftech.abc.dto.request; 
 
import com.accuresoftech.abc.enums.InvoiceStatus; 
import jakarta.validation.constraints.NotNull; 
import lombok.Data; 
 
import java.math.BigDecimal; 
import java.time.LocalDate; 
 
@Data 
public class InvoiceRequest { 
 
    // If creating normally: 
    @NotNull 
    private Long customerId; 
 
    @NotNull 
    private InvoiceStatus status; 
 
    @NotNull 
    private LocalDate issueDate; 
 
    @NotNull 
    private LocalDate dueDate; 
 
    @NotNull 
    private BigDecimal totalAmount; 
 
    @NotNull 
    private Long departmentId; 
 
    // optional: who created it (if null, service may set from context or proposal owner) 
    private Long createdBy; 
}