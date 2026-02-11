
package com.accuresoftech.abc.servicesimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.accuresoftech.abc.dto.request.ContactRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.ContactRepository;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.ContactService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // ---------------- CREATE ----------------
    @Override
    public ContactResponse createContact(Long customerId, ContactRequest request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Contact contact = Contact.builder()
                .customer(customer)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(request.getRole())
                .isPrimary(request.isPrimary())
                .deleted(false)
                .build();

        return toResponse(contactRepository.save(contact));
    }
    
    
    @Override
    public ContactResponse getContactById(Long id) {
     
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
     
        if (contact.isDeleted()) {
            throw new ResourceNotFoundException("Contact is deactivated");
        }
     
        return toResponse(contact);
    }

    // ---------------- GET BY CUSTOMER ----------------
    @Override
    public List<ContactResponse> getContactsByCustomer(Long customerId) {
        return contactRepository.findByCustomer_IdAndDeletedFalse(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------------- GET ALL (ROLE BASED) ----------------
    @Override
    public List<ContactResponse> getAllContacts() {

        User user = getCurrentUser();
        RoleKey role = user.getRole().getKey();

        List<Contact> contacts = switch (role) {
            case ADMIN -> contactRepository.findAll()
                    .stream().filter(c -> !c.isDeleted()).toList();

            case SUB_ADMIN -> contactRepository
                    .findByCustomer_Department_IdAndDeletedFalse(
                            user.getDepartment().getId());

            case STAFF -> contactRepository
                    .findByCustomer_AssignedUser_IdAndDeletedFalse(user.getId());
        };

        return contacts.stream().map(this::toResponse).toList();
    }

    // ---------------- UPDATE ----------------
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

        return toResponse(contactRepository.save(contact));
    }

    // ---------------- DEACTIVATE ----------------
    @Override
    public void deactivateContact(Long id) {

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contact not found with id: " + id));

        contact.setDeleted(true);
        contactRepository.save(contact);
    }

    // ---------------- ACTIVATE ----------------
    @Override
    public void activateContact(Long id) {

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contact not found with id: " + id));

        contact.setDeleted(false);
        contactRepository.save(contact);
    }

    // ---------------- HARD DELETE (OPTIONAL) ----------------
    @Override
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactRepository.delete(contact);
    }

    private ContactResponse toResponse(Contact c) {
        return ContactResponse.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .phone(c.getPhone())
                .role(c.getRole())
                .isPrimary(c.isPrimary())
                .customerName(c.getCustomer().getName())
                .build();
    }
}