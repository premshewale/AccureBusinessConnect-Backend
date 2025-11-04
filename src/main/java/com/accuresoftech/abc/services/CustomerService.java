package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    // Create a new customer
    CustomerResponse createCustomer(CustomerRequest request);

    // Get all customers
    List<CustomerResponse> getAllCustomers();

    // Get customer by ID
    CustomerResponse getCustomerById(Long id);

    // Update customer
    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    // Delete customer by ID
    void deleteCustomer(Long id);
}
