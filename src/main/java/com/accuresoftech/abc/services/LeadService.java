package com.accuresoftech.abc.services;

import java.util.List;

import com.accuresoftech.abc.dto.request.LeadConversionRequest;
import com.accuresoftech.abc.dto.request.LeadRequest;
import com.accuresoftech.abc.dto.response.LeadResponse;

public interface LeadService 
{
	List<LeadResponse> getAllLeads();

	LeadResponse getLeadById(Long id);

	LeadResponse createLead(LeadRequest request);

	LeadResponse updateLead(Long id, LeadRequest request);

	// void deleteLead(Long id);
	
	LeadResponse deactivateLead(Long id);
	LeadResponse activateLead(Long id);


	LeadResponse convertLeadToCustomer(LeadConversionRequest request);

	// NEW
	LeadResponse updateLeadStatus(Long id, String status);
}