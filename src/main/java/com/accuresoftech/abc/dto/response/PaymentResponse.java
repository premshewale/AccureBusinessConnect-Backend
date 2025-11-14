package com.accuresoftech.abc.dto.response; 
 
import lombok.*; 
 
import java.math.BigDecimal; 
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.accuresoftech.abc.enums.InvoiceStatus; 
 
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class PaymentResponse { 
    private Long id; 
    private BigDecimal amount; 
    private LocalDate paymentDate; 
    private String method; 
    private InvoiceStatus invoiceStatus;
    private LocalDateTime createdAt;
    private Long invoiceId;
}