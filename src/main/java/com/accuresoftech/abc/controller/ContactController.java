	package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.ContactRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponse> createContact(
            @PathVariable Long customerId,
            @RequestBody ContactRequest request) {
        ContactResponse response = contactService.createContact(customerId, request);
        return ResponseEntity.status(201).body(response); // ✅ Proper for new resource
    }

    @GetMapping
    public ResponseEntity<List<ContactResponse>> getContactsByCustomer(
            @PathVariable Long customerId) {
        List<ContactResponse> contacts = contactService.getContactsByCustomer(customerId);
        return ResponseEntity.ok(contacts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(
            @PathVariable Long id,
            @RequestBody ContactRequest request) {
        ContactResponse updated = contactService.updateContact(id, request);
        return ResponseEntity.ok(updated);
    }
    
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateContact(
            @PathVariable Long customerId,
            @PathVariable Long id) {
        contactService.deactivateContact(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateContact(
            @PathVariable Long customerId,
            @PathVariable Long id) {
        contactService.activateContact(id);
        return ResponseEntity.ok().build();
    }

    /*@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build(); // ✅ Proper for delete
    }*/
}