package com.accuresoftech.abc.servicesimpl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accuresoftech.abc.dto.request.LeadConversionRequest;
import com.accuresoftech.abc.dto.request.LeadRequest;
import com.accuresoftech.abc.dto.response.LeadResponse;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.Lead;
import com.accuresoftech.abc.entity.auth.LeadActivity;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.CustomerStatus;
import com.accuresoftech.abc.enums.CustomerType;
import com.accuresoftech.abc.enums.LeadStatus;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.LeadActivityRepository;
import com.accuresoftech.abc.repository.LeadRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.LeadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

	private final LeadRepository leadRepository;
	private final CustomerRepository customerRepository;
	private final UserRepository userRepository;
	private final DepartmentRepository departmentRepository;
	private final LeadActivityRepository leadActivityRepository;

	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	private boolean canAccessLead(Lead lead, User currentUser) {
		String role = currentUser.getRole().getKey().name();

		switch (role) {
		case "ADMIN":
			return true;
		case "SUB_ADMIN":
			if (lead.getOwner() != null && lead.getOwner().getDepartment() != null) {
				return lead.getOwner().getDepartment().getId().equals(currentUser.getDepartment().getId());
			}
			return false;
		case "STAFF":
			return (lead.getOwner() != null && lead.getOwner().getId().equals(currentUser.getId()))
					|| (lead.getAssignedTo() != null && lead.getAssignedTo().getId().equals(currentUser.getId()));
		default:
			return false;
		}
	}

	@Override
	public List<LeadResponse> getAllLeads() {
	    User currentUser = getCurrentUser();
	    String role = currentUser.getRole().getKey().name();

	    List<Lead> leads;

	    switch (role) {
	        case "ADMIN":
	            leads = leadRepository.findAll();
	            break;

	        case "SUB_ADMIN":
	            leads = leadRepository.findByDepartmentId(
	                    currentUser.getDepartment().getId());
	            break;

	        case "STAFF":
	            leads = leadRepository.findByOwnerId(
	                    currentUser.getId());
	            break;

	        default:
	            leads = List.of();
	    }

	    //  IMPORTANT: inactive (LOST) leads filter
	    return leads.stream()
	            .filter(l -> l.getStatus() != LeadStatus.LOST)
	            .map(this::toLeadResponse)
	            .toList();
	}

	@Override
	public LeadResponse getLeadById(Long id) {
		Lead lead = leadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

		if (lead.isDeleted()) {
			throw new RuntimeException("This lead has been deleted");
		}

		User currentUser = getCurrentUser();
		if (!canAccessLead(lead, currentUser)) {
			throw new RuntimeException("Access denied to this lead");
		}

		return toLeadResponse(lead);
	}

	@Override
	public LeadResponse createLead(LeadRequest request) {
		User currentUser = getCurrentUser();

		User owner = userRepository.findById(request.getOwnerId())
				.orElseThrow(() -> new ResourceNotFoundException("Owner user not found"));

		Department department = departmentRepository.findById(request.getDepartmentId())
				.orElseThrow(() -> new ResourceNotFoundException("Department not found"));

		if (currentUser.getRole().getKey() == RoleKey.STAFF) {
			department = currentUser.getDepartment();
		}

		if (request.getEmail() != null && leadRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Lead already exists with this email");
		}
		if (request.getPhone() != null && leadRepository.existsByPhone(request.getPhone())) {
			throw new RuntimeException("Lead already exists with this phone");
		}

		User assignedToUser = null;
		if (request.getAssignedTo() != null) {
			assignedToUser = userRepository.findById(request.getAssignedTo())
					.orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
		}

		if (owner.getDepartment() != null && !owner.getDepartment().getId().equals(department.getId())) {
			throw new RuntimeException("Owner does not belong to the selected department");
		}

		if (currentUser.getRole().getKey() == RoleKey.STAFF && !request.getOwnerId().equals(currentUser.getId())) {
			throw new RuntimeException("Staff can only create leads for themselves");
		}

		Lead lead = Lead.builder().name(request.getName()).email(request.getEmail()).phone(request.getPhone())
				.source(request.getSource()).status(LeadStatus.NEW).owner(owner).department(department)
				.assignedTo(assignedToUser).build();

		Lead savedLead = leadRepository.save(lead);

		logActivity(savedLead, "CREATE", null, LeadStatus.NEW.name(), "Lead created successfully");

		return toLeadResponse(savedLead);
	}

	@Override
	public LeadResponse updateLead(Long id, LeadRequest request) {
		Lead lead = leadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

		if (lead.isDeleted()) {
			throw new RuntimeException("This lead has been deleted");
		}

		User currentUser = getCurrentUser();
		if (!canAccessLead(lead, currentUser)) {
			throw new RuntimeException("Access denied to this lead");
		}

		if (request.getName() != null) {
			lead.setName(request.getName());
		}
		if (request.getEmail() != null) {
			lead.setEmail(request.getEmail());
		}
		if (request.getPhone() != null) {
			lead.setPhone(request.getPhone());
		}
		if (request.getSource() != null) {
			lead.setSource(request.getSource());
		}

		if (request.getAssignedTo() != null) {
			User assigned = userRepository.findById(request.getAssignedTo())
					.orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
			lead.setAssignedTo(assigned);
		}

		Lead updatedLead = leadRepository.save(lead);
		return toLeadResponse(updatedLead);
	}

	@Override
	@Transactional
	public LeadResponse convertLeadToCustomer(LeadConversionRequest request) {
		Lead lead = leadRepository.findById(request.getLeadId())
				.orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

		User currentUser = getCurrentUser();
		if (!canAccessLead(lead, currentUser)) {
			throw new RuntimeException("Access denied to this lead");
		}

		if (lead.getStatus() != LeadStatus.QUALIFIED && lead.getStatus() != LeadStatus.WON) {
			throw new RuntimeException("Only QUALIFIED or WON leads can be converted");
		}

		Customer customer = Customer.builder().name(lead.getName()).email(lead.getEmail()).phone(lead.getPhone())
				.industry(request.getIndustry()).address(request.getAddress()).website(request.getWebsite())
				.type(CustomerType.valueOf(request.getCustomerType())).status(CustomerStatus.ACTIVE)
				.assignedUser(lead.getOwner()).department(lead.getDepartment()).contactPersonCount(1)
				.tags("Converted from Lead").build();

		Customer savedCustomer = customerRepository.save(customer);

		lead.setStatus(LeadStatus.WON);
		lead.setCustomer(savedCustomer);
		leadRepository.save(lead);

		logActivity(lead, "CONVERT_TO_CUSTOMER", lead.getStatus().name(), LeadStatus.WON.name(),
				"Lead converted to customer");

		LeadResponse response = toLeadResponse(lead);
		response.setCustomerId(savedCustomer.getId());
		return response;
	}

	@Override
	public LeadResponse updateLeadStatus(Long id, String status) {
		Lead lead = leadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

		if (lead.isDeleted()) {
			throw new RuntimeException("This lead has been deleted");
		}

		User currentUser = getCurrentUser();
		if (!canAccessLead(lead, currentUser)) {
			throw new RuntimeException("Access denied to this lead");
		}

		LeadStatus newStatus;
		try {
			newStatus = LeadStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid lead status: " + status);
		}

		LeadStatus currentStatus = lead.getStatus();
		if (currentStatus == LeadStatus.NEW && newStatus == LeadStatus.WON) {
			throw new RuntimeException("Cannot move directly from NEW to WON");
		}
		if (currentStatus == LeadStatus.CONTACTED && newStatus == LeadStatus.WON) {
			throw new RuntimeException("Lead must be QUALIFIED before WON");
		}

		lead.setStatus(newStatus);
		Lead updated = leadRepository.save(lead);

		//  Log status update
		logActivity(lead, "STATUS_UPDATE", currentStatus.name(), newStatus.name(), "Lead status updated");

		//  NEW: Auto-convert to Customer when lead becomes WON
		if (newStatus == LeadStatus.WON && lead.getCustomer() == null) {
			Customer customer = Customer.builder().name(lead.getName()).email(lead.getEmail()).phone(lead.getPhone())
					.industry("Auto").address("Auto-generated from Lead").website("N/A").type(CustomerType.REGULAR)
					.status(CustomerStatus.ACTIVE).assignedUser(lead.getOwner()).department(lead.getDepartment())
					.tags("Auto-converted from Lead").build();

			Customer savedCustomer = customerRepository.save(customer);
			lead.setCustomer(savedCustomer);
			leadRepository.save(lead);

			logActivity(lead, "AUTO_CONVERT", currentStatus.name(), newStatus.name(),
					"Lead auto-converted to customer");
		}

		return toLeadResponse(updated);
	}


	private void logActivity(Lead lead, String action, String oldStatus, String newStatus, String remarks) {
		User currentUser = getCurrentUser();
		LeadActivity activity = LeadActivity.builder().lead(lead).action(action).oldStatus(oldStatus)
				.newStatus(newStatus).remarks(remarks).performedBy(currentUser).build();
		leadActivityRepository.save(activity);
	}

	private LeadResponse toLeadResponse(Lead lead) {
		return LeadResponse.builder().id(lead.getId()).name(lead.getName()).email(lead.getEmail())
				.phone(lead.getPhone()).source(lead.getSource() != null ? lead.getSource().name() : null)
				.status(lead.getStatus() != null ? lead.getStatus().name() : null)
				.ownerName(lead.getOwner() != null ? lead.getOwner().getName() : null)
				.assignedToName(lead.getAssignedTo() != null ? lead.getAssignedTo().getName() : null)
				.departmentName(lead.getDepartment() != null ? lead.getDepartment().getName() : null)
				.customerId(lead.getCustomer() != null ? lead.getCustomer().getId() : null)
				.createdAt(lead.getCreatedAt()).build();
	}

		@Override
		public LeadResponse deactivateLead(Long id) {
		    Lead lead = leadRepository.findById(id)
		            .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));
	
		    User currentUser = getCurrentUser();
		    if (!canAccessLead(lead, currentUser)) {
		        throw new RuntimeException("Access denied");
		    }
	
		    if (lead.getStatus() == LeadStatus.LOST) {
		        throw new RuntimeException("Lead already inactive");
		    }
	
		    lead.setStatus(LeadStatus.LOST);
		    leadRepository.save(lead);
	
		    return toLeadResponse(lead);
		}


		@Override
		public LeadResponse activateLead(Long id) {
		    Lead lead = leadRepository.findById(id)
		            .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

		    User currentUser = getCurrentUser();
		    if (!canAccessLead(lead, currentUser)) {
		        throw new RuntimeException("Access denied");
		    }

		    if (lead.getStatus() != LeadStatus.LOST) {
		        throw new RuntimeException("Lead already active");
		    }

		    lead.setStatus(LeadStatus.NEW);
		    leadRepository.save(lead);

		    return toLeadResponse(lead);
		}

}