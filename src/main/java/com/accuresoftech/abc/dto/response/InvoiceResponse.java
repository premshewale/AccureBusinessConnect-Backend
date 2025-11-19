package com.accuresoftech.abc.dto.response; 
 
import lombok.*; 
 
import java.math.BigDecimal; 
import java.time.LocalDate; 
import java.time.LocalDateTime; 
import java.util.List; 
 
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class InvoiceResponse { 
    private Long id; 
    private Long proposalId;           // optional 
    private String customerName; 
    private String departmentName; 
    private String createdByName; 
    private String status; 
    private LocalDate issueDate; 
    private LocalDate dueDate; 
    private BigDecimal totalAmount; 
    private List<PaymentResponse> payments; 
    private LocalDateTime createdAt; 
    
}