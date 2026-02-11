package com.accuresoftech.abc.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accuresoftech.abc.dto.response.CustomPageResponse;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.dto.response.LeadResponse;
<<<<<<< HEAD
import com.accuresoftech.abc.dto.response.ProposalResponse;
=======
>>>>>>> 7a6374bf518042b48b39ab21e46d9e95ad0b70d1
import com.accuresoftech.abc.services.CustomerService;
import com.accuresoftech.abc.services.LeadService;
import com.accuresoftech.abc.services.ProposalService;
import com.accuresoftech.abc.services.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")

public class ReportController {

    
    private final LeadService leadService;
    
    private final CustomerService customerService;
    
	private final ProposalService proposalService;


    
    @GetMapping("/leads")
    public ResponseEntity<List<LeadResponse>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }
    
    @GetMapping("/customers")
    public CustomPageResponse<CustomerResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return customerService.getAll(pageable, search);
    }
    
    @GetMapping("/proposals")
	public ResponseEntity<List<ProposalResponse>> getAllProposals() {
		return ResponseEntity.ok(proposalService.getAllProposals());
	}
    
}

