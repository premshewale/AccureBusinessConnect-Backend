package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.PaymentRequest;
import com.accuresoftech.abc.dto.response.PaymentResponse;
import com.accuresoftech.abc.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ✅ Get all payments (Admin/Sub-Admin)
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // ✅ Get payments linked to a specific invoice
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByInvoice(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(paymentService.getPaymentsByInvoice(invoiceId));
    }

    // ✅ Get a specific payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // ✅ Create new payment
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Update payment (optional)
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(response);
    }

    // ✅ Delete payment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
