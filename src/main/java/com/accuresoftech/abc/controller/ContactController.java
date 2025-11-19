package com.accuresoftech.abc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.accuresoftech.abc.dto.request.ContactRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.services.ContactService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    // ---------------------------------------------------------
    //  A) CUSTOMER-SPECIFIC CONTACTS
    // ---------------------------------------------------------

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<ContactResponse> createContact(
            @PathVariable Long customerId,
            @RequestBody ContactRequest request) {

        return ResponseEntity
                .status(201)
                .body(contactService.createContact(customerId, request));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ContactResponse>> getContactsByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(contactService.getContactsByCustomer(customerId));
    }


    // ---------------------------------------------------------
    //  B) GLOBAL CONTACTS (ADMIN)
    // ---------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }


    // ---------------------------------------------------------
    // COMMON UPDATE / DELETE
    // ---------------------------------------------------------

    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(
            @PathVariable Long id,
            @RequestBody ContactRequest request) {

        return ResponseEntity.ok(contactService.updateContact(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}