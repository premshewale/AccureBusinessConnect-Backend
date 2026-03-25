package com.accuresoftech.abc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.accuresoftech.abc.entity.auth.Payment;
import com.accuresoftech.abc.enums.PaymentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoiceId(Long invoiceId);
    Optional<Payment> findByGatewayOrderId(String orderId);
    @Query("SELECT p.method, COUNT(p) FROM Payment p GROUP BY p.method")
    List<Object[]> countPaymentsByMethod();
    Optional<Payment> findByInvoiceIdAndStatus(Long invoiceId, PaymentStatus status);
}
