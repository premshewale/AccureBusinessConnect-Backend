package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.InvoiceRequest;
import com.accuresoftech.abc.dto.response.InvoiceResponse;
import com.accuresoftech.abc.dto.response.PaymentResponse;
import com.accuresoftech.abc.entity.auth.*;
import com.accuresoftech.abc.enums.InvoiceStatus;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.*;
import com.accuresoftech.abc.services.InvoiceService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

	private final InvoiceRepository invoiceRepository;
	private final CustomerRepository customerRepository;
	private final DepartmentRepository departmentRepository;
	private final UserRepository userRepository;
	private final ProposalRepository proposalRepository;

	@Override
	public List<InvoiceResponse> getAllInvoices() {
		return invoiceRepository.findAll().stream().map(this::toInvoiceResponse).toList();
	}

	@Override
	public InvoiceResponse getInvoiceById(Long id) {
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
		return toInvoiceResponse(invoice);
	}

	@Override
	@Transactional
	public InvoiceResponse createInvoice(InvoiceRequest request) {

		Customer customer = customerRepository.findById(request.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		Department department = departmentRepository.findById(request.getDepartmentId())
				.orElseThrow(() -> new ResourceNotFoundException("Department not found"));

		User createdBy = userRepository.findById(request.getCreatedBy())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Proposal proposal = null;
		if (request.getProposalId() != null) {
			proposal = proposalRepository.findById(request.getProposalId())
					.orElseThrow(() -> new ResourceNotFoundException("Proposal not found"));
		}

		Invoice invoice = Invoice.builder().customer(customer).status(request.getStatus())
				.issueDate(request.getIssueDate()).dueDate(request.getDueDate()).totalAmount(request.getTotalAmount())
				.department(department).createdBy(createdBy).proposal(proposal) // ★ Linking proposal
				.build();

		Invoice saved = invoiceRepository.save(invoice);
		return toInvoiceResponse(saved);
	}

	@Override
	@Transactional
	public InvoiceResponse updateInvoice(Long id, InvoiceRequest request) {
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

		if (request.getStatus() != null)
			invoice.setStatus(request.getStatus());
		if (request.getIssueDate() != null)
			invoice.setIssueDate(request.getIssueDate());
		if (request.getDueDate() != null)
			invoice.setDueDate(request.getDueDate());
		if (request.getTotalAmount() != null)
			invoice.setTotalAmount(request.getTotalAmount());
		if (request.getProposalId() != null) {
			Proposal proposal = proposalRepository.findById(request.getProposalId())
					.orElseThrow(() -> new ResourceNotFoundException("Proposal not found"));
			invoice.setProposal(proposal);
		}

		Invoice updated = invoiceRepository.save(invoice);
		return toInvoiceResponse(updated);
	}

	@Override
	public InvoiceResponse updateInvoiceStatus(Long id, String status) {
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

		try {
			InvoiceStatus newStatus = InvoiceStatus.valueOf(status.toUpperCase());
			invoice.setStatus(newStatus);
		} catch (Exception e) {
			throw new RuntimeException("Invalid invoice status: " + status);
		}

		Invoice updated = invoiceRepository.save(invoice);
		return toInvoiceResponse(updated);
	}
	

	@Override
	public void deleteInvoice(Long id) {
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
		invoiceRepository.delete(invoice);
	}
	
	

	// ------------------- RESPONSE MAPPERS -------------------

	private InvoiceResponse toInvoiceResponse(Invoice invoice) {
		return InvoiceResponse.builder().id(invoice.getId())
				.proposalId(invoice.getProposal() != null ? invoice.getProposal().getId() : null)
				.customerName(invoice.getCustomer().getName())
				.departmentName(invoice.getDepartment() != null ? invoice.getDepartment().getName() : null)
				.createdByName(invoice.getCreatedBy() != null ? invoice.getCreatedBy().getName() : null)
				.status(invoice.getStatus().name()).issueDate(invoice.getIssueDate()).dueDate(invoice.getDueDate())
				.totalAmount(invoice.getTotalAmount())
				.payments(invoice.getPayments() != null
						? invoice.getPayments().stream().map(this::toPaymentResponse).toList()
						: null)
				.createdAt(invoice.getCreatedAt()).build();
	}

	private PaymentResponse toPaymentResponse(Payment payment) {
	    return PaymentResponse.builder()
	            .id(payment.getId())
	            .amount(payment.getAmount())
	            .paymentDate(payment.getPaymentDate())
	            .method(payment.getMethod().name())
	            .invoiceStatus(payment.getInvoice().getStatus())
	            .createdAt(payment.getCreatedAt())
	            .invoiceId(payment.getInvoice().getId())   //  VERY IMPORTANT 
	            .build();
	}
}