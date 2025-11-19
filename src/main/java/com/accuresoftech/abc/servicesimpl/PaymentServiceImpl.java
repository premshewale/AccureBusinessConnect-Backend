package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.PaymentRequest;
import com.accuresoftech.abc.dto.response.PaymentResponse;
import com.accuresoftech.abc.entity.auth.Invoice;
import com.accuresoftech.abc.entity.auth.Payment;
import com.accuresoftech.abc.enums.InvoiceStatus;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.InvoiceRepository;
import com.accuresoftech.abc.repository.PaymentRepository;
import com.accuresoftech.abc.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toPaymentResponse)
                .toList();
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return toPaymentResponse(payment);
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setMethod(request.getMethod());
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        
        // Add to invoice payments list (prevents null list issues for some JPA configs)
        if (invoice.getPayments() != null) {
            invoice.getPayments().add(payment);
        }

        Payment saved = paymentRepository.save(payment);

        // Update invoice status after payment
        updateInvoiceStatusAfterPayment(invoice);

        return toPaymentResponse(saved);
    }

    private void updateInvoiceStatusAfterPayment(Invoice invoice) {

        List<Payment> payments =
                invoice.getPayments() != null ? invoice.getPayments() : List.of();

        BigDecimal totalPaid = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = invoice.getTotalAmount();

        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(InvoiceStatus.UNPAID);
        } else if (totalPaid.compareTo(totalAmount) < 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PAID);
        }

        invoiceRepository.save(invoice);
    }

    @Override
    public PaymentResponse updatePayment(Long id, PaymentRequest request) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (request.getMethod() != null) payment.setMethod(request.getMethod());
        if (request.getAmount() != null) payment.setAmount(request.getAmount());
        if (request.getPaymentDate() != null) payment.setPaymentDate(request.getPaymentDate());

        Payment updated = paymentRepository.save(payment);

        // Update invoice status again after editing payment
        updateInvoiceStatusAfterPayment(payment.getInvoice());

        return toPaymentResponse(updated);
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        Invoice invoice = payment.getInvoice();

        paymentRepository.delete(payment);

        // After delete → status should update also
        updateInvoiceStatusAfterPayment(invoice);
    }

    @Override
    public List<PaymentResponse> getPaymentsByInvoice(Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return paymentRepository.findByInvoiceId(invoiceId)
                .stream()
                .map(this::toPaymentResponse)
                .toList();
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .method(payment.getMethod().name())   // ★ FIX
                .invoiceStatus(payment.getInvoice().getStatus())
                .createdAt(payment.getCreatedAt())
                .invoiceId(payment.getInvoice().getId())
                .build();
    }
}