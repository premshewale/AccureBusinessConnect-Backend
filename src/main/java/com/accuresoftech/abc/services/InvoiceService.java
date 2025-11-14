package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.InvoiceRequest;
import com.accuresoftech.abc.dto.response.InvoiceResponse;

import java.util.List;

public interface InvoiceService {
    List<InvoiceResponse> getAllInvoices();
    InvoiceResponse getInvoiceById(Long id);
    InvoiceResponse createInvoice(InvoiceRequest request);
    InvoiceResponse updateInvoice(Long id, InvoiceRequest request);
    void deleteInvoice(Long id);
    InvoiceResponse updateInvoiceStatus(Long id, String status);
}
