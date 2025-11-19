package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.PaymentRequest;
import com.accuresoftech.abc.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentResponse> getAllPayments();
    PaymentResponse getPaymentById(Long id);
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse updatePayment(Long id, PaymentRequest request);
    void deletePayment(Long id);

    // 👇 This is likely in your interface
    List<PaymentResponse> getPaymentsByInvoice(Long invoiceId);
}
