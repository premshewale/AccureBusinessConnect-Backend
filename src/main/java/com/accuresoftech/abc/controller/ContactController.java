package com.accuresoftech.abc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.accuresoftech.abc.dto.request.ContactRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.services.ContactService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers/{customerId}/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    // Create a new contact for a customer
    @PostMapping
    public ResponseEntity<ContactResponse> createContact(
            @PathVariable Long customerId,
            @RequestBody ContactRequest request) {
        ContactResponse response = contactService.createContact(customerId, request);
        return ResponseEntity.status(201).body(response); // HTTP 201 for new resource
    }

    // Get all contacts for a customer
    @GetMapping
    public ResponseEntity<List<ContactResponse>> getContactsByCustomer(
            @PathVariable Long customerId) {
        List<ContactResponse> contacts = contactService.getContactsByCustomer(customerId);
        return ResponseEntity.ok(contacts);
    }

    // Update a contact for a customer
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(
            @PathVariable Long customerId, // required for path mapping
            @PathVariable Long id,
            @RequestBody ContactRequest request) {
        ContactResponse updated = contactService.updateContact(id, request);
        return ResponseEntity.ok(updated);
    }

    // Delete a contact for a customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable Long customerId, // required for path mapping
            @PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build(); // HTTP 204 for delete
    }
}