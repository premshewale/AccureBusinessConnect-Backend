/*package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.ContactRepository;
import com.accuresoftech.abc.services.CustomerService;
import com.accuresoftech.abc.utils.AuthUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private AuthUtils authUtils;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
    	
    	 User currentUser = authUtils.getCurrentUser();
         if (currentUser == null) {
             throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
         }
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIndustry(request.getIndustry());
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());
        customer.setWebsite(request.getWebsite());
        customer.setContactPersonCount(request.getContactPersonCount());
        
        
         // Assign logged-in user as owner
        customer.setOwner(currentUser);
        
         // Optional: Assign department same as staff
        customer.setDepartment(currentUser.getDepartment());

        
        
        
        

        // Save Customer first
        Customer savedCustomer = customerRepository.save(customer);

        // Map contacts
        if (request.getContacts() != null) {
            List<Contact> contacts = request.getContacts().stream().map(c -> {
                Contact contact = new Contact();
                contact.setCustomer(savedCustomer);

                contact.setFirstName(c.getFirstName());
                contact.setLastName(c.getLastName());
                contact.setEmail(c.getEmail());
                contact.setPhone(c.getPhone());
                contact.setRole(Contact.Role.valueOf(c.getRole()));
                contact.setPrimary(c.isPrimary());
                return contact;
            }).collect(Collectors.toList());

            contactRepository.saveAll(contacts);
            savedCustomer.setContacts(contacts);
        }

        return toResponse(savedCustomer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id).map(this::toResponse).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIndustry(request.getIndustry());
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());
        customer.setWebsite(request.getWebsite());
        customer.setContactPersonCount(request.getContactPersonCount());

        Customer updated = customerRepository.save(customer);
        return toResponse(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setIndustry(customer.getIndustry());
        response.setType(customer.getType().name());
        response.setStatus(customer.getStatus().name());
        response.setWebsite(customer.getWebsite());
        response.setContactPersonCount(customer.getContactPersonCount());

        if (customer.getContacts() != null) {
            response.setContacts(customer.getContacts().stream().map(contact -> {
                CustomerResponse.ContactResponse cRes = new CustomerResponse.ContactResponse();
                cRes.setId(contact.getId());
                cRes.setFirstName(contact.getFirstName());
                cRes.setLastName(contact.getLastName());
                cRes.setEmail(contact.getEmail());
                cRes.setPhone(contact.getPhone());
                cRes.setRole(contact.getRole().name());
                cRes.setPrimary(contact.isPrimary());
                return cRes;
            }).collect(Collectors.toList()));
        }

        return response;
    }
}





package com.accuresoftech.abc.servicesimpl;

//import com.accuresoftech.abc.dto.request.ContactRequest;
import com.accuresoftech.abc.dto.request.CustomerRequest;
//import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.ContactRepository;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.services.CustomerService;
import com.accuresoftech.abc.utils.AuthUtils;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
	@Autowired
    private CustomerRepository customerRepository;
	@Autowired
    private ContactRepository contactRepository;
	@Autowired
    private AuthUtils authUtils;

    @Override
    public CustomerResponse create(CustomerRequest request) {
        User currentUser = authUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Unauthorized");
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .industry(request.getIndustry())
                .type(request.getType())
                .status(request.getStatus())
                .website(request.getWebsite())
                .owner(currentUser)
                .department(currentUser.getDepartment())
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        if (request.getContacts() != null) {
            List<Contact> contacts = request.getContacts().stream().map(c -> {
                Contact contact = new Contact();
                contact.setCustomer(savedCustomer);
                contact.setFirstName(c.getFirstName());
                contact.setLastName(c.getLastName());
                contact.setEmail(c.getEmail());
                contact.setPhone(c.getPhone());
                contact.setRole(Contact.Role.valueOf(c.getRole().toUpperCase()));
                contact.setPrimary(c.isPrimary());
                return contact;
            }).collect(Collectors.toList());
            contactRepository.saveAll(contacts);
            savedCustomer.setContacts(contacts);
        }

        return toResponse(savedCustomer);
    }

    @Override
    public List<CustomerResponse> getAll() {
        User currentUser = authUtils.getCurrentUser();
        if (currentUser == null) throw new AccessDeniedException("Unauthorized");

        List<Customer> customers;
        if (currentUser.getRole().getKey().name().equals("ADMIN")) {
            customers = customerRepository.findAll();
        } else {
            customers = customerRepository.findByOwnerId(currentUser.getId());
        }

        return customers.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getById(Long id) {
        User currentUser = authUtils.getCurrentUser();
        if (currentUser == null) throw new AccessDeniedException("Unauthorized");

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!currentUser.getRole().getKey().name().equals("ADMIN") &&
                !customer.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        return toResponse(customer);
    }

    private CustomerResponse toResponse(Customer c) {
        CustomerResponse res = new CustomerResponse();
        res.setId(c.getId());
        res.setName(c.getName());
        res.setEmail(c.getEmail());
        res.setPhone(c.getPhone());
        res.setAddress(c.getAddress());
        res.setIndustry(c.getIndustry());
        res.setType(c.getType());
        res.setStatus(c.getStatus());
        res.setWebsite(c.getWebsite());
        res.setOwnerName(c.getOwner() != null ? c.getOwner().getName() : null);
        res.setDepartmentName(c.getDepartment() != null ? c.getDepartment().getName() : null);

        if (c.getContacts() != null) {
            List<ContactResponse> contacts = c.getContacts().stream().map(contact -> {
                ContactResponse cr = new ContactResponse();
                cr.setId(contact.getId());
                cr.setFirstName(contact.getFirstName());
                cr.setLastName(contact.getLastName());
                cr.setEmail(contact.getEmail());
                cr.setPhone(contact.getPhone());
                cr.setRole(contact.getRole().name());
                cr.setPrimary(contact.isPrimary());
                return cr;
            }).collect(Collectors.toList());
            res.setContacts(contacts);
        }

        return res;
    }
}*/














package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.ContactRepository;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.services.CustomerService;
import com.accuresoftech.abc.utils.AuthUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final AuthUtils authUtils;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        User currentUser = authUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Unauthorized");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIndustry(request.getIndustry());
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());
        customer.setWebsite(request.getWebsite());

        // Assign owner & department
        customer.setOwner(currentUser);
        customer.setDepartment(currentUser.getDepartment());

        Customer savedCustomer = customerRepository.save(customer);

        // Map contacts
        if (request.getContacts() != null) {
            List<Contact> contacts = request.getContacts().stream().map(c -> {
                Contact contact = new Contact();
                contact.setCustomer(savedCustomer);
                contact.setFirstName(c.getFirstName());
                contact.setLastName(c.getLastName());
                contact.setEmail(c.getEmail());
                contact.setPhone(c.getPhone());
                contact.setRole(Contact.Role.valueOf(c.getRole().toUpperCase()));
                contact.setPrimary(c.isPrimary());
                return contact;
            }).collect(Collectors.toList());
            contactRepository.saveAll(contacts);
            savedCustomer.setContacts(contacts);
        }

        return toResponse(savedCustomer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        User currentUser = authUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Unauthorized");
        }

        List<Customer> customers;
        if (currentUser.getRole().getKey() == RoleKey.ADMIN) {
            customers = customerRepository.findAll();
        } else {
            // Staff sees only their customers
            customers = customerRepository.findByOwnerId(currentUser.getId());
        }

        return customers.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        User currentUser = authUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Unauthorized");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Check access
        if (currentUser.getRole().getKey() != RoleKey.ADMIN &&
                !customer.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        return toResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        User currentUser = authUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Unauthorized");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Only admin or owner can update
        if (currentUser.getRole().getKey() != RoleKey.ADMIN &&
                !customer.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIndustry(request.getIndustry());
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());
        customer.setWebsite(request.getWebsite());

        Customer updated = customerRepository.save(customer);

        // Update contacts
        if (request.getContacts() != null) {
            // Delete old contacts
            if (updated.getContacts() != null) {
                contactRepository.deleteAll(updated.getContacts());
            }
            List<Contact> contacts = request.getContacts().stream().map(c -> {
                Contact contact = new Contact();
                contact.setCustomer(updated);
                contact.setFirstName(c.getFirstName());
                contact.setLastName(c.getLastName());
                contact.setEmail(c.getEmail());
                contact.setPhone(c.getPhone());
                contact.setRole(Contact.Role.valueOf(c.getRole().toUpperCase()));
                contact.setPrimary(c.isPrimary());
                return contact;
            }).collect(Collectors.toList());
            contactRepository.saveAll(contacts);
            updated.setContacts(contacts);
        }

        return toResponse(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        User currentUser = authUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Unauthorized");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (currentUser.getRole().getKey() != RoleKey.ADMIN &&
                !customer.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        customerRepository.delete(customer);
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setIndustry(customer.getIndustry());
        response.setType(customer.getType() != null ? customer.getType().name() : null);
        response.setStatus(customer.getStatus() != null ? customer.getStatus().name() : null);
        response.setWebsite(customer.getWebsite());
        response.setOwnerName(customer.getOwner() != null ? customer.getOwner().getName() : null);
        response.setDepartmentName(customer.getDepartment() != null ? customer.getDepartment().getName() : null);

        if (customer.getContacts() != null) {
            response.setContacts(customer.getContacts().stream().map(contact -> {
                CustomerResponse.ContactResponse cRes = new CustomerResponse.ContactResponse();
                cRes.setId(contact.getId());
                cRes.setFirstName(contact.getFirstName());
                cRes.setLastName(contact.getLastName());
                cRes.setEmail(contact.getEmail());
                cRes.setPhone(contact.getPhone());
                cRes.setRole(contact.getRole().name());
                cRes.setPrimary(contact.isPrimary());
                return cRes;
            }).collect(Collectors.toList()));
        }

        return response;
    }
}

