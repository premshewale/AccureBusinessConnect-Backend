package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    Page<CustomerResponse> getAll(Pageable pageable, String search);

    CustomerResponse getCustomerById(Long id);

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    void deleteCustomer(Long id);
    
    
    
    
}