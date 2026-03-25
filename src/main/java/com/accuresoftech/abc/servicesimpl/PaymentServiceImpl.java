package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.PaymentRequest;
import com.accuresoftech.abc.dto.request.PaymentVerificationRequest;
import com.accuresoftech.abc.dto.response.OrderResponse;
import com.accuresoftech.abc.dto.response.PaymentResponse;
import com.accuresoftech.abc.entity.auth.Invoice;
import com.accuresoftech.abc.entity.auth.Payment;
import com.accuresoftech.abc.enums.InvoiceStatus;
import com.accuresoftech.abc.enums.PaymentMethod;
import com.accuresoftech.abc.enums.PaymentStatus;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.InvoiceRepository;
import com.accuresoftech.abc.repository.PaymentRepository;
import com.accuresoftech.abc.services.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
	

	@Value("${razorpay.key_id}")
	private String apiKey;
	
	@Value("${razorpay.key_secret}")
	private String apiSercret;

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    
    private final RazorpayClient razorpayClient;

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
    
   
    @Transactional
    public OrderResponse createOrder(Long invoiceId) throws Exception {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // ✅ Check existing CREATED payment
        Optional<Payment> existing =
                paymentRepository.findByInvoiceIdAndStatus(invoiceId, PaymentStatus.CREATED);

        if (existing.isPresent()) {

            Payment payment = existing.get();

            long amountInPaise = payment.getAmount()
                    .multiply(new BigDecimal(100))
                    .longValue();

            OrderResponse response = new OrderResponse();
            response.setOrderId(payment.getGatewayOrderId());
            response.setCurrency(payment.getCurrency());
            response.setAmount(amountInPaise);

            System.out.println("Reusing existing order: " + payment.getGatewayOrderId());

            return response;
        }

        // ✅ Create new order only if not exists
        long amountInPaise = invoice.getTotalAmount()
                .multiply(new BigDecimal(100))
                .longValue();

        JSONObject options = new JSONObject();
        options.put("amount", amountInPaise);
        options.put("currency", "INR");
        options.put("receipt", "INV_" + invoice.getId());

        Order order = razorpayClient.orders.create(options);

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(invoice.getTotalAmount());
        payment.setCurrency("INR");
        payment.setGatewayOrderId(order.get("id").toString());
        payment.setStatus(PaymentStatus.CREATED);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setMethod(PaymentMethod.BANK_TRANSFER);
        payment.setPaymentDate(LocalDate.now());

        paymentRepository.save(payment);

        System.out.println("New order created: " + order.get("id"));

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.get("id").toString());
        response.setCurrency("INR");
        response.setAmount(amountInPaise);

        return response;
    }
    
    @Transactional
    public void verifyPayment(PaymentVerificationRequest request) {

        try {

            String orderId = request.getRazorpay_order_id();
            String paymentId = request.getRazorpay_payment_id();
            String signature = request.getRazorpay_signature();

            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            boolean isValid = Utils.verifyPaymentSignature(options, apiSercret);

            Payment payment = paymentRepository
                    .findByGatewayOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            if (isValid) {

                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setGatewayPaymentId(paymentId);
//                paymentRepository.save(payment);

                Invoice invoice = payment.getInvoice();
                invoice.setStatus(InvoiceStatus.PAID);
//                invoiceRepository.save(invoice);

            } else {

                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
            }

        } catch (Exception e) {
            throw new RuntimeException("Payment verification failed");
        }
    }
    
    public Map<String, Long> getPaymentMethodCounts() {

        List<Object[]> results = paymentRepository.countPaymentsByMethod();

        Map<String, Long> response = new HashMap<>();

        // Initialize all enums with 0
        for (PaymentMethod method : PaymentMethod.values()) {
            response.put(method.name(), 0L);
        }

        // Replace with actual counts
        for (Object[] row : results) {
            String method = row[0].toString();
            Long count = (Long) row[1];
            response.put(method, count);
        }

        return response;
    }
}