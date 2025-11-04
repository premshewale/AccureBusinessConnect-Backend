package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.ProposalRequest;
import com.accuresoftech.abc.dto.response.ProposalResponse;
import com.accuresoftech.abc.services.ProposalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proposals")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @GetMapping
    public ResponseEntity<List<ProposalResponse>> getAllProposals() {
        return ResponseEntity.ok(proposalService.getAllProposals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProposalResponse> getProposalById(@PathVariable Long id) {
        return ResponseEntity.ok(proposalService.getProposalById(id));
    }

    @PostMapping
    public ResponseEntity<ProposalResponse> createProposal(@Valid @RequestBody ProposalRequest request) {
        ProposalResponse response = proposalService.createProposal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);  // ✅ 201 CREATED
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProposalResponse> updateProposal(@PathVariable Long id, 
                                                          @Valid @RequestBody ProposalRequest request) {
        return ResponseEntity.ok(proposalService.updateProposal(id, request));  // ✅ 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProposal(@PathVariable Long id) {
        proposalService.deleteProposal(id);
        return ResponseEntity.noContent().build();  // ✅ 204 NO CONTENT
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProposalResponse> updateProposalStatus(@PathVariable Long id, 
                                                                @RequestParam String status) {
        return ResponseEntity.ok(proposalService.updateProposalStatus(id, status));  // ✅ 200 OK
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ProposalResponse>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(proposalService.getProposalsByCustomer(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProposalResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(proposalService.getProposalsByStatus(status));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ProposalResponse>> getByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(proposalService.getProposalsByDepartment(departmentId));
    }

    @GetMapping("/count/customer/{customerId}")
    public ResponseEntity<Long> countByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(proposalService.getProposalCountByCustomer(customerId));
    }
    
}