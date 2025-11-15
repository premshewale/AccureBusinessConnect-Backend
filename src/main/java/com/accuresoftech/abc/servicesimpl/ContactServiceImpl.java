package com.accuresoftech.abc.servicesimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.accuresoftech.abc.dto.request.ContactRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.ContactRepository;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.services.ContactService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

	private final ContactRepository contactRepository;
	private final CustomerRepository customerRepository;

	@Override
	public ContactResponse createContact(Long customerId, ContactRequest request) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		Contact contact = Contact.builder().customer(customer).firstName(request.getFirstName())
				.lastName(request.getLastName()).email(request.getEmail()).phone(request.getPhone())
				.role(request.getRole()).isPrimary(request.isPrimary()).build();

		Contact saved = contactRepository.save(contact);

		return toResponse(saved);
	}

	@Override
	public List<ContactResponse> getContactsByCustomer(Long customerId) {
		return contactRepository.findByCustomer_Id(customerId).stream().map(this::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public ContactResponse updateContact(Long id, ContactRequest request) {
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

		contact.setFirstName(request.getFirstName());
		contact.setLastName(request.getLastName());
		contact.setEmail(request.getEmail());
		contact.setPhone(request.getPhone());
		contact.setRole(request.getRole());
		contact.setPrimary(request.isPrimary());

		Contact updated = contactRepository.save(contact);
		return toResponse(updated);
	}

	@Override
	public void deleteContact(Long id) {
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
		contactRepository.delete(contact);
	}

	private ContactResponse toResponse(Contact c) {
		return ContactResponse.builder().id(c.getId()).firstName(c.getFirstName()).lastName(c.getLastName())
				.email(c.getEmail()).phone(c.getPhone()).role(c.getRole()).isPrimary(c.isPrimary())
				.customerName(c.getCustomer().getName()).build();
	}
}