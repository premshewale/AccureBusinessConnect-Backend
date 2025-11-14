package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.PaymentRequest;
import com.accuresoftech.abc.dto.response.PaymentResponse;
import com.accuresoftech.abc.entity.auth.Invoice;
import com.accuresoftech.abc.entity.auth.Payment;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.PaymentRepository;
import com.accuresoftech.abc.repository.InvoiceRepository;
import com.accuresoftech.abc.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
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

        Payment payment = Payment.builder()
                .invoice(invoice)
                .method(request.getMethod())
                .amount(request.getAmount())
                .paymentDate(request.getPaymentDate())
                .build();

        Payment saved = paymentRepository.save(payment);
        return toPaymentResponse(saved);
    }

    @Override
    public PaymentResponse updatePayment(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (request.getMethod() != null) payment.setMethod(request.getMethod());
        if (request.getAmount() != null) payment.setAmount(request.getAmount());
        if (request.getPaymentDate() != null) payment.setPaymentDate(request.getPaymentDate());

        Payment updated = paymentRepository.save(payment);
        return toPaymentResponse(updated);
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        paymentRepository.delete(payment);
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
                .invoiceId(payment.getInvoice().getId())
                .invoiceStatus(payment.getInvoice().getStatus())   // ENUM -> OK
                .amount(payment.getAmount())
                .method(payment.getMethod().name())                // ENUM → STRING
                .paymentDate(payment.getPaymentDate())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}