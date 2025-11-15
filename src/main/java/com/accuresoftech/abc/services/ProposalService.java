package com.accuresoftech.abc.services;

import java.util.List;

import com.accuresoftech.abc.dto.request.ProposalRequest;
import com.accuresoftech.abc.dto.response.ProposalResponse;

public interface ProposalService {
	List<ProposalResponse> getAllProposals();

	ProposalResponse getProposalById(Long id);

	ProposalResponse createProposal(ProposalRequest request);

	ProposalResponse updateProposal(Long id, ProposalRequest request);

	void deleteProposal(Long id);

	ProposalResponse updateProposalStatus(Long id, String status);

	List<ProposalResponse> getProposalsByCustomer(Long customerId);

	List<ProposalResponse> getProposalsByStatus(String status);

	List<ProposalResponse> getProposalsByDepartment(Long departmentId);

	Long getProposalCountByCustomer(Long customerId);

}