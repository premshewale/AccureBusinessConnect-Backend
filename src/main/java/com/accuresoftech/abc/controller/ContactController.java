/*package com.accuresoftech.abc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accuresoftech.abc.dto.request.CustomerRequest.ContactRequest;
import com.accuresoftech.abc.dto.response.CustomerResponse.ContactResponse;
import com.accuresoftech.abc.services.ContactService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

	@Autowired
	private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ContactResponse create(@RequestBody ContactRequest request) {
        return contactService.createContact(request);
    }

    @PutMapping("/{id}")
    public ContactResponse update(@PathVariable Long id, @RequestBody ContactRequest request) {
        return contactService.updateContact(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contactService.deleteContact(id);
    }

    @GetMapping("/{id}")
    public ContactResponse getById(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<ContactResponse> getByCustomer(@PathVariable Long customerId) {
        return contactService.getContactsByCustomerId(customerId);
    }
}*/
