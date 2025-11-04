/*package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.services.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    // Constructor Injection (no Lombok)
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIndustry(request.getIndustry());
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());
        customer.setWebsite(request.getWebsite());
        customer.setTags(request.getTags());
        customer.setAssignedUserId(request.getAssignedUserId());
        customer.setDepartmentId(request.getDepartmentId());

        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponse(savedCustomer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return convertToResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existingCustomer.setName(request.getName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhone(request.getPhone());
        existingCustomer.setAddress(request.getAddress());
        existingCustomer.setIndustry(request.getIndustry());
        existingCustomer.setType(request.getType());
        existingCustomer.setStatus(request.getStatus());
        existingCustomer.setWebsite(request.getWebsite());
        existingCustomer.setTags(request.getTags());
        existingCustomer.setAssignedUserId(request.getAssignedUserId());
        existingCustomer.setDepartmentId(request.getDepartmentId());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    // ✅ Manual mapping method
    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setIndustry(customer.getIndustry());
        response.setType(customer.getType());
        response.setStatus(customer.getStatus());
        response.setWebsite(customer.getWebsite());
        response.setTags(customer.getTags());
        response.setAssignedUserId(customer.getAssignedUserId());
        response.setDepartmentId(customer.getDepartmentId());
        return response;
    }
}customer 1st impl*/




package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.ContactRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.enums.ContactRole;
import com.accuresoftech.abc.repository.ContactRepository;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.services.ContactService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;

    // ✅ Manual constructor injection (no Lombok)
    public ContactServiceImpl(ContactRepository contactRepository, CustomerRepository customerRepository) {
        this.contactRepository = contactRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ContactResponse createContact(ContactRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + request.getCustomerId()));

        Contact contact = new Contact();
        contact.setCustomer(customer);
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setRole(request.getRole());
        contact.setIsPrimary(request.getIsPrimary());

        Contact savedContact = contactRepository.save(contact);
        return convertToResponse(savedContact);
    }

    @Override
    public List<ContactResponse> getAllContacts() {
        return contactRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ContactResponse getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));
        return convertToResponse(contact);
    }

    @Override
    public ContactResponse updateContact(Long id, ContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + request.getCustomerId()));

        contact.setCustomer(customer);
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setRole(request.getRole());
        contact.setIsPrimary(request.getIsPrimary());

        Contact updatedContact = contactRepository.save(contact);
        return convertToResponse(updatedContact);
    }

    @Override
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));
        contactRepository.delete(contact);
    }

    // ✅ Mapping helper
    private ContactResponse convertToResponse(Contact contact) {
        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setCustomerId(contact.getCustomer().getId());
        response.setCustomerName(contact.getCustomer().getName()); // Assuming Customer entity has 'name' field
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setPhone(contact.getPhone());
        response.setRole(contact.getRole() != null ? contact.getRole().name() : null);
        response.setIsPrimary(contact.getIsPrimary());
        return response;
    }
}
