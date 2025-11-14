package com.accuresoftech.abc.entity.auth;

import com.accuresoftech.abc.entity.BaseEntity;

import com.accuresoftech.abc.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;   // CASH, BANK_TRANSFER, CREDIT_CARD, UPI, CHEQUE

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate paymentDate;
}
