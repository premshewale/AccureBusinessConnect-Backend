/*package com.accuresoftech.abc.servicesimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accuresoftech.abc.dto.request.CustomerRequest.ContactRequest;
import com.accuresoftech.abc.dto.response.CustomerResponse.ContactResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.repository.ContactRepository;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.services.ContactService;

import mapper.ContactMapper;



@Service
public class ContactServiceImpl implements ContactService{
	
	 private final ContactRepository contactRepository;
		
	    private final CustomerRepository customerRepository;
	    
	    private final ContactMapper contactMapper;

	    public ContactServiceImpl(ContactRepository contactRepository, CustomerRepository customerRepository, ContactMapper contactMapper) {
	        this.contactRepository = contactRepository;
	        this.customerRepository = customerRepository;
	        this.contactMapper = contactMapper;
	    }

	    @Override
	    public ContactResponse createContact(ContactRequest request) {
	        Customer customer = customerRepository.findById(request.getCustomerId())
	                .orElseThrow(() -> new RuntimeException("Customer not found"));

	        Contact contact = contactMapper.toEntity(request, customer);
	        return contactMapper.toResponse(contactRepository.save(contact));
	    }

	    @Override
	    public ContactResponse updateContact(Long id, ContactRequest request) {
	        Contact existing = contactRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Contact not found"));

	        Customer customer = customerRepository.findById(request.getCustomerId())
	                .orElseThrow(() -> new RuntimeException("Customer not found"));

	        contactMapper.updateEntity(existing, request, customer);
	        return contactMapper.toResponse(contactRepository.save(existing));
	    }

	    @Override
	    public void deleteContact(Long id) {
	        contactRepository.deleteById(id);
	    }

	    @Override
	    public ContactResponse getContactById(Long id) {
	        Contact contact = contactRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Contact not found"));
	        return contactMapper.toResponse(contact);
	    }

	    @Override
	    public List<ContactResponse> getContactsByCustomerId(Long customerId) {
	        Customer customer = customerRepository.findById(customerId)
	                .orElseThrow(() -> new RuntimeException("Customer not found"));
	        return contactRepository.findByCustomer(customer)
	                .stream()
	                .map(contactMapper::toResponse)
	                .collect(Collectors.toList());
	    }
}*/



