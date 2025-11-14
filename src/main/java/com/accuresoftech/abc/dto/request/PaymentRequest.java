package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequest {

    @NotNull
    private Long invoiceId;

    @NotNull
    private PaymentMethod method;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate paymentDate;
}
