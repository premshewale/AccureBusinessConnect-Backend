package com.accuresoftech.abc.servicesimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.CustomPageResponse;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.CustomerStatus;
import com.accuresoftech.abc.enums.CustomerType;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.ContactRepository;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.services.CustomerService;
import com.accuresoftech.abc.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	private final ContactRepository contactRepository;
	private final AuthUtils authUtils;

	private User getCurrentUser() {
		return authUtils.getCurrentUser();
	}

	private boolean canAccessCustomer(Customer c, User u) {
		if (u.getRole().getKey() == RoleKey.ADMIN) {
			return true;
		}
		if (u.getRole().getKey() == RoleKey.SUB_ADMIN) {
			return c.getDepartment().getId().equals(u.getDepartment().getId());
		}
		return c.getAssignedUser().getId().equals(u.getId());
	}

	@Override
	public CustomerResponse createCustomer(CustomerRequest request) {
		User currentUser = getCurrentUser();

		// C: Prevent duplicate by email/phone
		if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Customer already exists with this email");
		}
		if (request.getPhone() != null && customerRepository.existsByPhone(request.getPhone())) {
			throw new RuntimeException("Customer already exists with this phone");
		}

		// A: Add default values for type/status if null
		Customer customer = Customer.builder().name(request.getName()).email(request.getEmail())
				.phone(request.getPhone()).address(request.getAddress()).industry(request.getIndustry())
				.website(request.getWebsite())
				.type(request.getType() != null ? request.getType() : CustomerType.REGULAR)
				.status(request.getStatus() != null ? request.getStatus() : CustomerStatus.ACTIVE)
				.contactPersonCount(request.getContactPersonCount()).tags(request.getTags())
				.department(currentUser.getDepartment()).assignedUser(currentUser).build();

		Customer saved = customerRepository.save(customer);

		// Handle optional contacts
		if (request.getContacts() != null) {
			List<Contact> contacts = request.getContacts().stream().map(contactReq -> {
				Contact contact = new Contact();
				contact.setCustomer(saved);
				contact.setFirstName(contactReq.getFirstName());
				contact.setLastName(contactReq.getLastName());
				contact.setEmail(contactReq.getEmail());
				contact.setPhone(contactReq.getPhone());
				contact.setRole(contactReq.getRole());
				contact.setPrimary(contactReq.isPrimary());
				return contact;
			}).collect(Collectors.toList());
			contactRepository.saveAll(contacts);
			saved.setContacts(contacts);
		}

		return toResponse(saved);
	}

	
	@Override
	public CustomPageResponse<CustomerResponse> getAll(Pageable pageable, String search) {

	    User currentUser = getCurrentUser();
	    Page<Customer> page;

	    if (currentUser.getRole().getKey() == RoleKey.ADMIN) {
	        page = (search != null && !search.isEmpty())
	                ? customerRepository.searchGlobal(search, pageable)
	                : customerRepository.findAll(pageable);
	    } else if (currentUser.getRole().getKey() == RoleKey.SUB_ADMIN) {
	        page = customerRepository.findByDepartment_Id(currentUser.getDepartment().getId(), pageable);
	    } else {
	        page = customerRepository.findByAssignedUserId(currentUser.getId(), pageable);
	    }

	    List<CustomerResponse> list = page.getContent()
	                                      .stream()
	                                      .map(this::toResponse)
	                                      .collect(Collectors.toList());

	    return CustomPageResponse.<CustomerResponse>builder()
	            .content(list)
	            .totalElements(page.getTotalElements())
	            .totalPages(page.getTotalPages())
	            .build();
	}

	
	@Override
	public CustomerResponse getCustomerById(Long id) {
		User currentUser = getCurrentUser();
		if (currentUser == null) {
			throw new AccessDeniedException("Unauthorized");
		}

		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));

		if (!canAccessCustomer(customer, currentUser)) {
			throw new AccessDeniedException("Access Denied");
		}

		return toResponse(customer);

	}

	@Override
	public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
		User currentUser = getCurrentUser();

		Customer c = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		if (!canAccessCustomer(c, currentUser)) {
			throw new AccessDeniedException("Access denied");
		}

		c.setName(request.getName());
		c.setEmail(request.getEmail());
		c.setPhone(request.getPhone());
		c.setAddress(request.getAddress());
		c.setIndustry(request.getIndustry());
		c.setWebsite(request.getWebsite());
		c.setType(request.getType());
		c.setStatus(request.getStatus());
		c.setContactPersonCount(request.getContactPersonCount());
		c.setTags(request.getTags());

		Customer updated = customerRepository.save(c);
		return toResponse(updated);
	}

	@Override
	public void deleteCustomer(Long id) {
		User currentUser = getCurrentUser();
		if (currentUser == null) {
			throw new AccessDeniedException("Unauthorized");
		}

		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));

		if (!canAccessCustomer(customer, currentUser)) {
			throw new AccessDeniedException("Access Denied");
		}

		// D: Soft delete instead of hard delete
		customer.setDeleted(true);
		customerRepository.save(customer);
	}

	private CustomerResponse toResponse(Customer c) {
		return CustomerResponse.builder().id(c.getId()).name(c.getName()).email(c.getEmail()).phone(c.getPhone())
				.address(c.getAddress()).industry(c.getIndustry()).type(c.getType()).status(c.getStatus())
				.website(c.getWebsite()).contactPersonCount(c.getContactPersonCount()).tags(c.getTags())
				.assignedUserName(c.getAssignedUser() != null ? c.getAssignedUser().getName() : null)
				.departmentName(c.getDepartment() != null ? c.getDepartment().getName() : null)
				.totalContacts(c.getContacts() != null ? c.getContacts().size() : 0).createdAt(c.getCreatedAt())
				.updatedAt(c.getUpdatedAt()).build();
	}
}