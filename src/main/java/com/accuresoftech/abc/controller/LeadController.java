package com.accuresoftech.abc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accuresoftech.abc.dto.request.LeadConversionRequest;
import com.accuresoftech.abc.dto.request.LeadRequest;
import com.accuresoftech.abc.dto.request.LeadStatusRequest;
import com.accuresoftech.abc.dto.response.LeadResponse;
import com.accuresoftech.abc.services.LeadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @GetMapping
    public ResponseEntity<List<LeadResponse>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponse> getLeadById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    @PostMapping
    public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody LeadRequest request) {
        LeadResponse response = leadService.createLead(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadResponse> updateLead(@PathVariable Long id,
                                                   @Valid @RequestBody LeadRequest request) {
        return ResponseEntity.ok(leadService.updateLead(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<LeadResponse> updateLeadStatus(@PathVariable Long id,
                                                         @Valid @RequestBody LeadStatusRequest statusRequest) {
        LeadResponse response = leadService.updateLeadStatus(id, statusRequest.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/convert")
    public ResponseEntity<LeadResponse> convertToCustomer(@PathVariable Long id,
                                                          @Valid @RequestBody LeadConversionRequest request) {
        request.setLeadId(id);
        LeadResponse response = leadService.convertLeadToCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<LeadResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.deactivateLead(id));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<LeadResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.activateLead(id));
    }

    
    
}