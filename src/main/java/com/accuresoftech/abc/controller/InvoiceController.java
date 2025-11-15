package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.InvoiceRequest;
import com.accuresoftech.abc.dto.response.InvoiceResponse;
import com.accuresoftech.abc.services.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        InvoiceResponse response = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable Long id,
                                                         @Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InvoiceResponse> updateInvoiceStatus(@PathVariable Long id,
                                                               @RequestParam String status) {
        return ResponseEntity.ok(invoiceService.updateInvoiceStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
