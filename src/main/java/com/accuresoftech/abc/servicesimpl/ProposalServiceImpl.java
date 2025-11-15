package com.accuresoftech.abc.servicesimpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accuresoftech.abc.dto.request.ProposalRequest;
import com.accuresoftech.abc.dto.response.ProposalResponse;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.Proposal;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.ProposalStatus;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.ProposalRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.ProposalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

	private final ProposalRepository proposalRepository;
	private final CustomerRepository customerRepository;
	private final DepartmentRepository departmentRepository;
	private final UserRepository userRepository;

	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	private boolean canAccessProposal(Proposal proposal, User currentUser) {
		String role = currentUser.getRole().getKey().name();

		switch (role) {
		case "ADMIN":
			return true;
		case "SUB_ADMIN":
			return proposal.getOwner() != null && proposal.getOwner().getDepartment() != null
					&& proposal.getOwner().getDepartment().getId().equals(currentUser.getDepartment().getId());
		case "STAFF":
			return proposal.getOwner() != null && proposal.getOwner().getId().equals(currentUser.getId());
		default:
			return false;
		}
	}

	// GET ALL PROPOSALS
	@Override
	public List<ProposalResponse> getAllProposals() {
		User currentUser = getCurrentUser();
		String role = currentUser.getRole().getKey().name();

		List<Proposal> proposals;

		switch (role) {
		case "ADMIN":
			proposals = proposalRepository.findByDeletedFalse();
			break;
		case "SUB_ADMIN":
			proposals = proposalRepository.findByDepartmentIdAndDeletedFalse(currentUser.getDepartment().getId());
			break;
		case "STAFF":
			proposals = proposalRepository.findByOwnerIdAndDeletedFalse(currentUser.getId());
			break;
		default:
			proposals = List.of();
		}

		return proposals.stream().map(this::toProposalResponse).toList();
	}

	// GET BY ID
	@Override
	public ProposalResponse getProposalById(Long id) {
		Proposal proposal = proposalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proposal not found"));

		User currentUser = getCurrentUser();
		if (!canAccessProposal(proposal, currentUser)) {
			throw new RuntimeException("Access denied to this proposal");
		}

		return toProposalResponse(proposal);
	}

	// CREATE PROPOSAL
	@Override
	public ProposalResponse createProposal(ProposalRequest request) {
		User currentUser = getCurrentUser();

		// Input validation
		if (request.getBudget() == null || request.getBudget().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Budget must be greater than zero");
		}

		if (request.getDeadline() == null || request.getDeadline().isBefore(java.time.LocalDate.now())) {
			throw new IllegalArgumentException("Deadline must be a future date");
		}

		User owner = userRepository.findById(request.getOwnerId())
				.orElseThrow(() -> new ResourceNotFoundException("Owner user not found"));

		Customer customer = customerRepository.findById(request.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		Department department = departmentRepository.findById(request.getDepartmentId())
				.orElseThrow(() -> new ResourceNotFoundException("Department not found"));

		if (currentUser.getRole().getKey() == RoleKey.STAFF && !request.getOwnerId().equals(currentUser.getId())) {
			throw new RuntimeException("Staff can only create proposals for themselves");
		}

		Proposal proposal = Proposal.builder().description(request.getDescription()).budget(request.getBudget())
				.deadline(request.getDeadline()).status(ProposalStatus.DRAFT).customer(customer).department(department)
				.owner(owner).build();

		Proposal savedProposal = proposalRepository.save(proposal);
		return toProposalResponse(savedProposal);
	}

	// UPDATE STATUS WITH RULES
	@Transactional
	@Override
	public ProposalResponse updateProposalStatus(Long id, String status) {
		Proposal proposal = proposalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proposal not found"));

		User currentUser = getCurrentUser();
		if (!canAccessProposal(proposal, currentUser)) {
			throw new RuntimeException("Access denied to this proposal");
		}

		ProposalStatus current = proposal.getStatus();
		ProposalStatus newStatus;

		try {
			newStatus = ProposalStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid proposal status: " + status);
		}

		// Restrict: Only manager/admin can send proposals
		if (newStatus == ProposalStatus.SENT && currentUser.getRole().getKey() != RoleKey.SUB_ADMIN
				&& currentUser.getRole().getKey() != RoleKey.ADMIN) {
			throw new RuntimeException("Only manager or admin can send proposals");
		}

		// Allowed transitions
		if (current == ProposalStatus.DRAFT && newStatus == ProposalStatus.SENT) {
			proposal.setStatus(ProposalStatus.SENT);
		} else if (current == ProposalStatus.SENT && newStatus == ProposalStatus.ACCEPTED) {
			proposal.setStatus(ProposalStatus.ACCEPTED);
			// Future hook → invoiceService.createInvoiceFromProposal(proposal);
		} else if (current == ProposalStatus.SENT && newStatus == ProposalStatus.REJECTED) {
			proposal.setStatus(ProposalStatus.REJECTED);
		} else {
			throw new RuntimeException("Invalid status transition from " + current + " to " + newStatus);
		}

		Proposal updated = proposalRepository.save(proposal);
		return toProposalResponse(updated);
	}

	// UPDATE PROPOSAL DETAILS
	@Override
	public ProposalResponse updateProposal(Long id, ProposalRequest request) {
		Proposal proposal = proposalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proposal not found"));

		User currentUser = getCurrentUser();
		if (!canAccessProposal(proposal, currentUser)) {
			throw new RuntimeException("Access denied to this proposal");
		}

		proposal.setDescription(request.getDescription());
		proposal.setBudget(request.getBudget());
		proposal.setDeadline(request.getDeadline());

		if (request.getCustomerId() != null) {
			Customer customer = customerRepository.findById(request.getCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
			proposal.setCustomer(customer);
		}

		if (request.getDepartmentId() != null) {
			Department department = departmentRepository.findById(request.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
			proposal.setDepartment(department);
		}

		if (request.getOwnerId() != null) {
			User owner = userRepository.findById(request.getOwnerId())
					.orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
			proposal.setOwner(owner);
		}

		Proposal updatedProposal = proposalRepository.save(proposal);
		return toProposalResponse(updatedProposal);
	}

	// SOFT DELETE
	@Override
	public void deleteProposal(Long id) {
		Proposal proposal = proposalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proposal not found"));

		User currentUser = getCurrentUser();
		if (!canAccessProposal(proposal, currentUser)) {
			throw new RuntimeException("Access denied to this proposal");
		}

		proposal.setDeleted(true);
		proposalRepository.save(proposal);
	}

	// RESPONSE MAPPER

	private ProposalResponse toProposalResponse(Proposal proposal) {
		return ProposalResponse.builder().id(proposal.getId()).description(proposal.getDescription())
				.budget(proposal.getBudget()).status(proposal.getStatus() != null ? proposal.getStatus().name() : null)
				.deadline(proposal.getDeadline())
				.customerName(proposal.getCustomer() != null ? proposal.getCustomer().getName() : null)
				.departmentName(proposal.getDepartment() != null ? proposal.getDepartment().getName() : null)

				// FIX: Combine first + last name safely
				.ownerName(proposal.getOwner() != null ? proposal.getOwner().getName() : null)
				.createdAt(proposal.getCreatedAt()).build();
	}

	// CUSTOM FETCHES
	@Override
	public List<ProposalResponse> getProposalsByCustomer(Long customerId) {
		User currentUser = getCurrentUser();
		List<Proposal> proposals = proposalRepository.findByCustomerIdAndDeletedFalse(customerId);
		return proposals.stream().filter(p -> canAccessProposal(p, currentUser)).map(this::toProposalResponse).toList();
	}

	@Override
	public List<ProposalResponse> getProposalsByStatus(String status) {
		ProposalStatus proposalStatus;
		try {
			proposalStatus = ProposalStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid proposal status: " + status);
		}

		User currentUser = getCurrentUser();
		List<Proposal> proposals;

		switch (currentUser.getRole().getKey()) {
		case ADMIN:
			proposals = proposalRepository.findByStatusAndDeletedFalse(proposalStatus);
			break;
		case SUB_ADMIN:
			proposals = proposalRepository.findByStatusAndDepartment_IdAndDeletedFalse(proposalStatus,
					currentUser.getDepartment().getId());
			break;
		case STAFF:
			proposals = proposalRepository.findByOwnerIdAndDeletedFalse(currentUser.getId());
			break;
		default:
			proposals = List.of();
		}

		return proposals.stream().map(this::toProposalResponse).toList();
	}

	@Override
	public List<ProposalResponse> getProposalsByDepartment(Long departmentId) {
		User currentUser = getCurrentUser();

		if (currentUser.getRole().getKey() == RoleKey.STAFF) {
			throw new RuntimeException("Staff cannot view department proposals");
		}

		List<Proposal> proposals = proposalRepository.findByDepartmentIdAndDeletedFalse(departmentId);
		return proposals.stream().filter(p -> canAccessProposal(p, currentUser)).map(this::toProposalResponse).toList();
	}

	@Override
	public Long getProposalCountByCustomer(Long customerId) {
		User currentUser = getCurrentUser();
		List<Proposal> proposals = proposalRepository.findByCustomerIdAndDeletedFalse(customerId);
		return proposals.stream().filter(p -> canAccessProposal(p, currentUser)).count();
	}
}