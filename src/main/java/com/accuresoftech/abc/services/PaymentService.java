package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.PaymentRequest;
import com.accuresoftech.abc.dto.request.PaymentVerificationRequest;
import com.accuresoftech.abc.dto.response.OrderResponse;
import com.accuresoftech.abc.dto.response.PaymentResponse;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

public interface PaymentService {
    List<PaymentResponse> getAllPayments();
    PaymentResponse getPaymentById(Long id);
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse updatePayment(Long id, PaymentRequest request);
    void deletePayment(Long id);

    // 👇 This is likely in your interface
    List<PaymentResponse> getPaymentsByInvoice(Long invoiceId);
    public OrderResponse createOrder(Long invoiceId) throws Exception;
    public void verifyPayment(PaymentVerificationRequest request);
    public Map<String, Long> getPaymentMethodCounts();
   
}
