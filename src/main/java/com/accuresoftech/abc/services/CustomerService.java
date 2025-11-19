package com.accuresoftech.abc.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.CustomPageResponse;
import com.accuresoftech.abc.dto.response.CustomerResponse;

public interface CustomerService {

	CustomerResponse createCustomer(CustomerRequest request);

	// Page<CustomerResponse> getAll(Pageable pageable, String search);

	CustomerResponse getCustomerById(Long id);

	CustomerResponse updateCustomer(Long id, CustomerRequest request);

	void deleteCustomer(Long id);
	
	CustomPageResponse<CustomerResponse> getAll(Pageable pageable, String search);

}